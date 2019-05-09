package io.github.wordandahalf.blueprint.annotations;

import io.github.wordandahalf.blueprint.classes.transformers.reference.FieldReferenceTransformer;
import io.github.wordandahalf.blueprint.classes.transformers.reference.MethodReferenceTransformer;
import io.github.wordandahalf.blueprint.logging.BlueprintLogger;
import io.github.wordandahalf.blueprint.classes.transformers.ClassTransformer;
import io.github.wordandahalf.blueprint.classes.transformers.method.MethodInjectionInfo;
import io.github.wordandahalf.blueprint.classes.transformers.method.MethodInjector;
import io.github.wordandahalf.blueprint.classes.transformers.method.MethodOverwriter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BlueprintAnnotationParser {
    /**
     * Scans the provided method for Blueprint-related annotations. The class declaration must be decorated with a {@link Blueprint} annotation for any other annotations to be processed.
     * @param clazz The class to scan
     * @return A list of the {@link ClassTransformer}s found in the class
     */
    public static List<ClassTransformer> parse(Class<?> clazz, Blueprint blueprint) {
        List<ClassTransformer> classTransformers = new ArrayList<>();

        for(Method m : clazz.getDeclaredMethods()) {
            BlueprintLogger.finer(BlueprintAnnotationParser.class, "Handling method " + m.getName());
            for(Annotation annotation : m.getAnnotations()) {
                if(annotation instanceof Inject) {
                    Inject inject = (Inject) annotation;

                    classTransformers.add(new MethodInjector(
                            new MethodInjectionInfo(m.getName(), inject.target(), inject.at()))
                    );

                    BlueprintLogger.finer(BlueprintAnnotationParser.class, "Added inject");
                } else
                if(annotation instanceof Overwrite) {
                    Overwrite overwrite = (Overwrite) annotation;

                    classTransformers.add(new MethodOverwriter(m.getName(), overwrite.target()));

                    BlueprintLogger.finer(BlueprintAnnotationParser.class, "Added overwrite");
                } else
                if(annotation instanceof Shadow) {
                    Shadow shadow = (Shadow) annotation;

                    classTransformers.add(new MethodReferenceTransformer(clazz.getName(), m.getName(), blueprint.target(), m.getName()));

                    BlueprintLogger.finer(BlueprintAnnotationParser.class, "Added shadow");
                }
            }
        }

        for(Field f : clazz.getDeclaredFields()) {
            BlueprintLogger.finer(BlueprintAnnotationParser.class, "Handling field " + f.getName());
            for(Annotation annotation : f.getAnnotations()) {
                if(annotation instanceof Shadow) {
                    Shadow shadow = (Shadow) annotation;

                    classTransformers.add(new FieldReferenceTransformer(clazz.getName(), f.getName(), blueprint.target(), f.getName()));

                    BlueprintLogger.finer(BlueprintAnnotationParser.class, "Added shadow");
                }
            }
        }

        return classTransformers;
    }
}
