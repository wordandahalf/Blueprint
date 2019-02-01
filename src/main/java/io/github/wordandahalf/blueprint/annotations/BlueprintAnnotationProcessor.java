package io.github.wordandahalf.blueprint.annotations;

import io.github.wordandahalf.blueprint.Blueprints;
import io.github.wordandahalf.blueprint.exceptions.PlanSignatureException;
import io.github.wordandahalf.blueprint.injection.InjectionHelper;
import io.github.wordandahalf.blueprint.utils.LoggingUtil;
import javassist.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class BlueprintAnnotationProcessor {
    /**
     * Determines if the provided annotation is a Blueprint injection annotation.
     * @param annotation The annotation
     * @return If the annotation is Blueprint related.
     */
    public static boolean isBlueprintAnnotation(Annotation annotation) {
        return annotation.annotationType().getPackage().getName()
                .equals(BlueprintAnnotationProcessor.class.getPackage().getName());
    }

    /**
     * Handles and processes an annotation for injection.
     * @param annotation The annotation to process
     * @param source The source method
     * @param target The fully-qualified name of the target class
     * @return The edited class for loading
     */
    public static CtClass handleAnnotation(Annotation annotation, Method source, String target) throws NotFoundException, PlanSignatureException, CannotCompileException {
        if(annotation instanceof Inject) {
            return handleInject((Inject) annotation, source, target);
        }
        if(annotation instanceof Overwrite) {
            return handleOverwrite((Overwrite) annotation, source, target);
        }

        return null;
    }

    private static CtClass handleInject(Inject inject, Method source, String target) throws NotFoundException, PlanSignatureException, CannotCompileException {
        if(Blueprints.DEBUG_ENABLED)
            LoggingUtil.getLogger().fine("Processing Inject annotation...");

        CtClass sourceClass = ClassPool.getDefault().get(source.getDeclaringClass().getName());
        CtClass targetClass = ClassPool.getDefault().get(target);

        CtMethod sourceMethod = sourceClass.getDeclaredMethod(source.getName());
        CtMethod targetMethod = targetClass.getDeclaredMethod(inject.target());

        At location = inject.at();

        CtClass editedClass  = null;

        switch(location.location().toUpperCase()) {
            case "HEAD":
                editedClass  = InjectionHelper.injectMethod(sourceMethod, targetMethod, InjectionHelper.InjectionLocation.INJECT_BEFORE);
                break;
            case "TAIL":
                editedClass  = InjectionHelper.injectMethod(sourceMethod, targetMethod, InjectionHelper.InjectionLocation.INJECT_AFTER);
                break;
            case "BEFORE_RETURN":
                throw new UnsupportedOperationException("@At(target = ..., at = @At(location = \"BEFORE_RETURN\")) is not implemented yet.");
            case "INVOCATION":
                throw new UnsupportedOperationException("@At(target = ..., at = @At(location = \"INVOCATION\")) is not implemented yet.");
            default:
                throw new UnsupportedOperationException("@At(target = ..., at = @At(location = \"" + location.location() + "\"))");
        }

        return editedClass;
    }

    private static CtClass handleOverwrite(Overwrite overwrite, Method source, String target) throws NotFoundException, PlanSignatureException, CannotCompileException {
        if(Blueprints.DEBUG_ENABLED)
            LoggingUtil.getLogger().fine("Processing Overwrite annotation...");

        CtClass sourceClass = ClassPool.getDefault().get(source.getDeclaringClass().getName());
        CtClass targetClass = ClassPool.getDefault().get(target);

        CtMethod sourceMethod = sourceClass.getDeclaredMethod(source.getName());
        CtMethod targetMethod = targetClass.getDeclaredMethod(overwrite.target());

        return InjectionHelper.overwriteMethod(sourceMethod, targetMethod);
    }
}
