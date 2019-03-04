package io.github.wordandahalf.blueprint.annotations;

import io.github.wordandahalf.blueprint.logging.BlueprintLogger;
import io.github.wordandahalf.blueprint.transformers.ClassTransformer;
import io.github.wordandahalf.blueprint.transformers.method.MethodInjectionInfo;
import io.github.wordandahalf.blueprint.transformers.method.MethodInjector;
import io.github.wordandahalf.blueprint.transformers.method.MethodOverwriter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BlueprintAnnotationParser {
    /**
     * Scans the provided method for Blueprint-related annotations. The class declaration must be decorated with a {@link Blueprint} annotation for any other annotations to be processed.
     * @param clazz The class to scan
     * @return A list of the {@link ClassTransformer}s found in the class
     */
    public static List<ClassTransformer> parse(Class<?> clazz) {
        List<ClassTransformer> classTransformers = new ArrayList<>();

        for(Method m : clazz.getDeclaredMethods()) {
            BlueprintLogger.log(Level.FINER, BlueprintAnnotationParser.class, "Handling method " + m.getName());
            for(Annotation annotation : m.getAnnotations()) {
                if(annotation instanceof Inject) {
                    Inject inject = (Inject) annotation;

                    classTransformers.add(new MethodInjector(
                            new MethodInjectionInfo(m.getName(), inject.target(), inject.at()))
                    );

                    BlueprintLogger.log(Level.FINER, BlueprintAnnotationParser.class, "Added inject");
                } else
                if(annotation instanceof Overwrite) {
                    Overwrite overwrite = (Overwrite) annotation;

                    classTransformers.add(new MethodOverwriter(m.getName(), overwrite.target()));

                    BlueprintLogger.log(Level.FINER, BlueprintAnnotationParser.class, "Added overwrite");
                }
            }
        }

        return classTransformers;
    }
}
