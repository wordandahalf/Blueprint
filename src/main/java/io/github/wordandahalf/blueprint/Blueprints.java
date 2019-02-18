package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotation.Blueprint;
import io.github.wordandahalf.blueprint.annotation.parsing.BlueprintAnnotationParser;
import io.github.wordandahalf.blueprint.transformers.ClassTransformer;
import io.github.wordandahalf.blueprint.utils.Pair;
import javassist.CannotCompileException;
import javassist.CtClass;

public class Blueprints {
    private static BlueprintClassTransformerPool transformerPool;
    private static BlueprintClassPool classPool;

    public static void add(Class<?> clazz) {
        transformerPool = new BlueprintClassTransformerPool();
        classPool = new BlueprintClassPool();

        Blueprint blueprint = clazz.getAnnotation(Blueprint.class);

        if(blueprint != null) {
            BlueprintAnnotationParser parser = new BlueprintAnnotationParser(clazz, blueprint);

            parser.parse();

            parser.getClassTransformers().forEach(transformer -> {
                transformerPool.add(parser.getSourceClassName(), parser.getTargetClassName(), transformer);
            });
        }
    }

    public static void apply() throws Exception {
        for(Pair<String, String> classPair : transformerPool.getClassPairs()) {
            for(ClassTransformer transformer : transformerPool.getTransformerByPair(classPair)) {
                    CtClass modifiedClass = transformer.apply(
                            classPool.getClass(classPair.left),
                            classPool.getClass(classPair.right)
                    );

                classPool.addModifiedClass(modifiedClass);
            }
        }

        loadModifiedClasses();
    }

    private static void loadModifiedClasses() throws CannotCompileException {
        for(CtClass clazz : classPool.getModifiedClasses()) {
            clazz.toClass();
        }
    }
}
