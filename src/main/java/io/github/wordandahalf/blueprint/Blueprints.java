package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotations.Blueprint;
import io.github.wordandahalf.blueprint.annotations.BlueprintAnnotationProcessor;
import io.github.wordandahalf.blueprint.exceptions.PlanSignatureException;
import io.github.wordandahalf.blueprint.utils.LoggingUtil;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class Blueprints {
    public static boolean DEBUG_ENABLED = true;

    /**
     * Loads a Blueprint-annotated class
     * @param clazz The class to load
     * @throws NotFoundException If the targeted class could not be found
     * @throws PlanSignatureException If the signatures of the source and target methods are not identical
     * @throws CannotCompileException If Javassist encounters an error when injecting.
     */
    public static void add(Class<?> clazz)  throws NotFoundException, PlanSignatureException, CannotCompileException {
        add(clazz, null, "");
    }

    /**
     * Loads a Blueprint-annotated class
     * @param clazz The class to load
     * @throws NotFoundException If the targeted class could not be found
     * @throws PlanSignatureException If the signatures of the source and target methods are not identical
     * @throws CannotCompileException If Javassist encounters an error when injecting.
     */
    public static void add(Class<?> clazz, ClassLoader loader, String classpath) throws NotFoundException, PlanSignatureException, CannotCompileException {
        ClassPool.getDefault().appendClassPath(classpath);

        Blueprint blueprint = clazz.getAnnotation(Blueprint.class);

        if(blueprint != null) {
            if(DEBUG_ENABLED)
                LoggingUtil.getLogger().fine("Found 'Blueprint' annotation on class '" + clazz.getSimpleName() + "'");

            CtClass editedClass = null;

            for(Method method : clazz.getMethods()) {
                for(Annotation annotation : method.getAnnotations()) {
                    if(BlueprintAnnotationProcessor.isBlueprintAnnotation(annotation)) {
                        if(DEBUG_ENABLED)
                            LoggingUtil.getLogger().fine("Found '" + annotation.annotationType().getSimpleName() + "' on method '" + method.getName() + "'");

                        editedClass = BlueprintAnnotationProcessor.handleAnnotation(annotation, method, blueprint.target());
                    }
                }
            }

            editedClass.toClass(loader, null);
        }
    }
}
