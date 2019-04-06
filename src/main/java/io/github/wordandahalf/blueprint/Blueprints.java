package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotations.Blueprint;
import io.github.wordandahalf.blueprint.annotations.BlueprintAnnotationParser;
import io.github.wordandahalf.blueprint.classes.BlueprintClass;
import io.github.wordandahalf.blueprint.classes.BlueprintClassPool;
import io.github.wordandahalf.blueprint.logging.BlueprintLogger;
import io.github.wordandahalf.blueprint.transformers.ClassTransformer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

public class Blueprints {
    public static boolean DEBUG = false;

    private static BlueprintClassPool classPool = new BlueprintClassPool();
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
        Annotation annotation = clazz.getAnnotation(Blueprint.class);

        if(annotation != null) {
            Blueprint blueprint = (Blueprint) annotation;

            List<ClassTransformer> parsedTransformers = BlueprintAnnotationParser.parse(clazz);
            BlueprintClass targetClass = null;

            // Load the source class if it is not already loaded
            if(classPool.getClass(clazz.getName()) == null) {
                try {
                    BlueprintClass sourceClass = new BlueprintClass(clazz.getName(), classLoader);

                    classPool.addClass(sourceClass);
                } catch (IOException e) {
                    BlueprintLogger.warn(Blueprints.class, "There was an unexpected error when source loading class '" + clazz.getName() + "': " + e.getMessage());
                }
            }

            // Load the target class if it is not already loaded
            if(classPool.getClass(blueprint.target()) == null) {
                try {
                    targetClass = new BlueprintClass(blueprint.target(), classLoader);

                    classPool.addClass(targetClass);
                } catch (IOException e) {
                    BlueprintLogger.warn(Blueprints.class, "There was an unexpected error when target loading class '" + blueprint.target() + "': " + e.getMessage());
                }
            } else {
                targetClass = classPool.getClass(blueprint.target());
            }

            BlueprintLogger.finest(Blueprints.class, "Loaded " + parsedTransformers.size() + " transformer(s) for class '" + targetClass.getClassName() + "'");
            targetClass.addTransformers(clazz.getName(), parsedTransformers.toArray(new ClassTransformer[] {}));

            classPool.addModifiedClass(targetClass);
        } else {
            System.err.println("The class " + clazz.getSimpleName() + " is not decorated by a Blueprint annotation!");
        }
    }

    /**
     * Applies the loaded {@link io.github.wordandahalf.blueprint.transformers.ClassTransformer}s and loads the modified classes
     */
    public static void apply() throws Exception {
        for(BlueprintClass clazz : classPool.getModifiedClasses()) {
            for(String sourceClassName : clazz.getTransformersSources()) {
                for(ClassTransformer transformer : clazz.getTransformersBySource(sourceClassName)) {
                    BlueprintLogger.fine(Blueprint.class, "Handling transformer " + transformer.getClass().getSimpleName() + " with source class '" + sourceClassName + "' and target class '" + clazz.getClassName() + "'");

                    BlueprintClass modifiedClass = transformer.apply(classPool.getClass(sourceClassName), clazz);

                    classPool.addModifiedClass(modifiedClass);
                }
            }
        }

        loadModifiedClasses();
    }

    private static void loadModifiedClasses() throws Exception {
        for(BlueprintClass clazz : classPool.getModifiedClasses()) {
            BlueprintLogger.fine(Blueprint.class, "Redefining class '" + clazz.getClassName());

            Class definedClass = clazz.defineClass();
        }

        BlueprintLogger.fine(Blueprint.class, "All " + classPool.getModifiedClasses().size() + " class(es) have been redefined.");
    }
}
