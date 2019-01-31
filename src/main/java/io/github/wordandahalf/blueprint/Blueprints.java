package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotations.Blueprint;
import io.github.wordandahalf.blueprint.annotations.Plan;
import io.github.wordandahalf.blueprint.annotations.PlanType;
import io.github.wordandahalf.blueprint.exceptions.PlanParameterException;
import io.github.wordandahalf.blueprint.exceptions.PlanSignatureException;
import io.github.wordandahalf.blueprint.injection.InjectionHelper;
import io.github.wordandahalf.blueprint.utils.LoggingUtil;
import javassist.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Blueprints {
    public static boolean DEBUG_ENABLED = true;

    private static ClassPool pool = ClassPool.getDefault();

    public static Class<?> add(Class<?> clazz, ClassLoader loader, String classpath) throws Exception {
        pool.appendClassPath(classpath);

        return add(clazz, loader);
    }

    public static Class<?> add(Class<?> clazz, ClassLoader loader) throws Exception {
        Annotation classAnnotation = clazz.getAnnotation(Blueprint.class);

        CtClass modifiedClass = null;

        if(classAnnotation != null) {
            Blueprint blueprint = (Blueprint) classAnnotation;

            for(Method method : clazz.getDeclaredMethods()) {
                Annotation methodAnnotation = method.getAnnotation(Plan.class);

                if(methodAnnotation != null) {
                    Plan plan = (Plan) methodAnnotation;

                    modifiedClass = handlePlan(blueprint, clazz, plan, method);
                }
            }
        }

        if(DEBUG_ENABLED)
            LoggingUtil.getLogger().fine("Loading modified class '" + modifiedClass.getName() + "'");

        return modifiedClass.toClass(loader, null);
    }

    public static Class<?> add(Class<?> clazz) throws Exception {
        return add(clazz, null);
    }

    private static boolean checkParameters(CtMethod a, CtMethod b) throws Exception {
       if ((a.getParameterTypes().length == b.getParameterTypes().length)
               && Arrays.equals(a.getParameterTypes(), b.getParameterTypes()))
                return true;

        return false;
    }

    private static CtClass handlePlan(Blueprint blueprint, Class<?> clazz, Plan plan, Method method) throws Exception {
        if(DEBUG_ENABLED)
            LoggingUtil.getLogger().fine("Handling annotated class '" + clazz.getName() + "' with method '" + method.getName() + "'");

        ClassPool pool = ClassPool.getDefault();

        CtClass targetCtClass = pool.get(blueprint.target());
        CtClass sourceCtClass = pool.get(clazz.getName());

        targetCtClass.defrost();

        if(DEBUG_ENABLED) {
            LoggingUtil.getLogger().fine("Found target class '" + targetCtClass.getName() + "'");
            LoggingUtil.getLogger().fine("Found source class '" + sourceCtClass.getName() + "'");
        }

        CtMethod targetCtMethod = targetCtClass.getDeclaredMethod(plan.method());
        CtMethod sourceCtMethod = sourceCtClass.getDeclaredMethod(method.getName());

        if(DEBUG_ENABLED) {
            LoggingUtil.getLogger().fine("Found target method '" + targetCtMethod.getName() + "'");
            LoggingUtil.getLogger().fine("Found source method '" + sourceCtMethod.getName() + "'");
        }

        if(DEBUG_ENABLED)
            LoggingUtil.getLogger().fine("Performing injection with source method '" + sourceCtMethod.getName() +
                                            "', target method '" + targetCtMethod.getName() + "' and target class '" + targetCtClass.getName() + "'");

        switch(plan.type()) {
            case INJECT_AFTER:
                InjectionHelper.injectMethod(targetCtMethod, sourceCtMethod, InjectionHelper.InjectionLocation.INJECT_AFTER);
                break;
            case INJECT_BEFORE:
                InjectionHelper.injectMethod(targetCtMethod, sourceCtMethod, InjectionHelper.InjectionLocation.INJECT_BEFORE);
                break;
            case OVERWRITE:
                InjectionHelper.overwriteMethod(targetCtMethod, sourceCtMethod);
            default:
                break;
        }

        if(DEBUG_ENABLED)
            LoggingUtil.getLogger().fine("Finished injection.");

        return targetCtClass;
    }
}
