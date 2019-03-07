package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotations.Blueprint;
import io.github.wordandahalf.blueprint.annotations.BlueprintAnnotationParser;
import io.github.wordandahalf.blueprint.classes.BlueprintClass;
import io.github.wordandahalf.blueprint.classes.BlueprintClassNodePool;
import io.github.wordandahalf.blueprint.classes.BlueprintClassTransformerPool;
import io.github.wordandahalf.blueprint.loading.BlueprintClassReader;
import io.github.wordandahalf.blueprint.loading.BlueprintClassWriter;
import io.github.wordandahalf.blueprint.logging.BlueprintLogger;
import io.github.wordandahalf.blueprint.transformers.ClassTransformer;
import io.github.wordandahalf.blueprint.utils.Pair;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class Blueprints {
    public static boolean DEBUG = true;

    private static BlueprintClassTransformerPool transformerPool = new BlueprintClassTransformerPool();
    private static BlueprintClassNodePool classNodePool = new BlueprintClassNodePool();
    private static ClassLoader classLoader = Blueprints.class.getClassLoader();

    public static void useClassLoader(ClassLoader loader) {
        classLoader = loader;
    }

    /**
     * Adds the provided class as a Blueprint injection class.
     * The provided class must be decorated with a {@link io.github.wordandahalf.blueprint.annotations.Blueprint} annotation for any other annotations to be processed.
     * Adding the same injection class multiple times is ignored.
     * To apply the loaded {@link io.github.wordandahalf.blueprint.transformers.ClassTransformer}s, see {@link #apply()}
     * @param clazz
     */
    public static void add(Class<?> clazz) {
        Annotation blueprint = clazz.getAnnotation(Blueprint.class);

        if(blueprint instanceof Blueprint) {
            if(transformerPool.getTransformersBySource(clazz.getName()).size() == 0) {
                List<ClassTransformer> parsedTransformers = BlueprintAnnotationParser.parse(clazz);
                transformerPool.add(clazz.getName(), ((Blueprint) blueprint).target(), parsedTransformers);
            } else {
                System.err.println("The class " + clazz.getSimpleName() + " has already been loaded!");
            }
        } else {
            System.err.println("The class " + clazz.getSimpleName() + " is not decorated by a Blueprint annotation!");
        }
    }

    /**
     * Applies the loaded {@link io.github.wordandahalf.blueprint.transformers.ClassTransformer}s and loads the modified classes
     */
    public static void apply() throws Exception {
        for(Pair<String, String> classNames : transformerPool.getClassPairs()) {
            for(ClassTransformer transformer : transformerPool.getTransformerByPair(classNames)) {

                BlueprintLogger.fine(Blueprint.class, "Handling transformer " + transformer.getClass().getSimpleName() + " with source class '" + classNames.left + "' and target class '" + classNames.right + "'");

                BlueprintClass sourceClass = classNodePool.getClass(classNames.left);
                BlueprintClass targetClass = classNodePool.getClass(classNames.right);

                if(sourceClass == null) {
                    sourceClass = new BlueprintClass(classNames.left, classLoader);

                    classNodePool.addClass(sourceClass);
                }

                if(targetClass == null) {
                    targetClass = new BlueprintClass(classNames.right, classLoader);

                    classNodePool.addClass(targetClass);
                }

                BlueprintClass modifiedClass = transformer.apply(sourceClass, targetClass);
                classNodePool.addModifiedClass(modifiedClass);
            }
        }

        loadModifiedClasses();
    }

    private static void loadModifiedClasses() throws Exception {
        BlueprintLogger.fine(Blueprint.class, "Reloading classes...");

        for(BlueprintClass blueprintClass : classNodePool.getModifiedClasses()) {
            BlueprintLogger.fine(Blueprint.class, "Redefining class '" + blueprintClass.getClassName());

            Class clazz = blueprintClass.defineClass();
        }

        BlueprintLogger.fine(Blueprint.class, "All " + classNodePool.getModifiedClasses().size() + " class(es) have been redefined.");
    }

    private static Class defineClass(ClassNode node) throws Exception {
        BlueprintClassWriter writer = new BlueprintClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS, classLoader);
        node.accept(writer);

        Class clazz = ClassLoader.class;

        Method method = clazz.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        method.setAccessible(true);

        byte[] bytecode = writer.toByteArray();

        Object result = method.invoke(classLoader, node.name.replace("/", "."), bytecode, 0, bytecode.length);

        return (Class) result;
    }
}
