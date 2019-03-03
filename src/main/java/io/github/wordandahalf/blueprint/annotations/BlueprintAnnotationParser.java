package io.github.wordandahalf.blueprint.annotations;

import io.github.wordandahalf.blueprint.transformers.ClassTransformer;
import io.github.wordandahalf.blueprint.transformers.method.MethodInjectionInfo;
import io.github.wordandahalf.blueprint.transformers.method.MethodInjector;
import io.github.wordandahalf.blueprint.transformers.method.MethodOverwriter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BlueprintAnnotationParser {
    /**
     * Scans the provided method for Blueprint-related annotations. The class declaration must be decorated with a {@link Blueprint} annotation for any other annotations to be processed.
     * @param clazz The class to scan
     * @return A list of the {@link ClassTransformer}s found in the class
     */
    public static List<ClassTransformer> parse(Class<?> clazz) {
        List<ClassTransformer> classTransformers = new ArrayList<>();

        for(Method m : clazz.getDeclaredMethods()) {
            //TODO: Use logging utility
            System.out.println("Handling method " + m.getName());
            for(Annotation annotation : m.getAnnotations()) {
                if(annotation instanceof Inject) {
                    Inject inject = (Inject) annotation;

                    classTransformers.add(new MethodInjector(
                            new MethodInjectionInfo(m.getName(), inject.target(), inject.at()))
                    );

                    System.out.println("Added inject");
                } else
                if(annotation instanceof Overwrite) {
                    Overwrite overwrite = (Overwrite) annotation;

                    classTransformers.add(new MethodOverwriter(m.getName(), overwrite.target()));

                    System.out.println("Added overwrite");
                }
            }
        }

        return classTransformers;
    }
}
