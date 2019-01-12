package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotations.Blueprint;
import io.github.wordandahalf.blueprint.annotations.Plan;
import io.github.wordandahalf.blueprint.annotations.PlanType;
import io.github.wordandahalf.blueprint.exceptions.PlanParameterException;
import io.github.wordandahalf.blueprint.exceptions.PlanSignatureException;
import io.github.wordandahalf.blueprint.utils.LoggingUtil;
import javassist.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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

        return loadModifiedClass(modifiedClass, loader);
    }

    public static Class<?> add(Class<?> clazz) throws Exception {
        return add(clazz, null);
    }

    private static boolean checkParameters(CtMethod a, CtMethod b) throws Exception {
        if(a.getParameterTypes().length != b.getParameterTypes().length)
            return false;

        for(int i = 0; i < a.getParameterTypes().length; i++)
            if(!(a.getParameterTypes()[i].getName()
                    .equals(b.getParameterTypes()[i].getName())))
                return false;

        return true;
    }

    private static Class<?> loadModifiedClass(CtClass ctClass, ClassLoader loader) throws CannotCompileException  {
        if(DEBUG_ENABLED)
            LoggingUtil.getLogger().fine("Loading modified class '" + ctClass.getName() + "'");

        return ctClass.toClass(loader, null);
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

        if(!checkParameters(targetCtMethod, sourceCtMethod))
            throw new PlanParameterException(sourceCtMethod.getName(), targetCtMethod.getParameterTypes());

        if(plan.type().equals(PlanType.OVERWRITE))
            if(!targetCtMethod.getSignature().equals(sourceCtMethod.getSignature()))
                throw new PlanSignatureException(plan, sourceCtMethod.getName(), targetCtMethod.getSignature());

        String newMethodName = "blueprint$" + targetCtMethod.getName() + "_" + System.currentTimeMillis();
        targetCtMethod.setName(newMethodName);

        if(DEBUG_ENABLED && (targetCtMethod.getName().equals(newMethodName)))
            LoggingUtil.getLogger().fine("Renamed target method '" + plan.method() + "' to '" + newMethodName + "'");

        CtMethod newCtMethod = CtNewMethod.copy(sourceCtMethod, plan.method(), targetCtClass, null);

        String newMethodCall = newMethodName + "(";

        int numberOfParameters = targetCtMethod.getParameterTypes().length;

        if(numberOfParameters > 0) {
            for (int i = 0; i < numberOfParameters; i++) {
                //$0 is the class instance
                newMethodCall += "$" + (i + 1) + ",";
            }

            //To get rid of the trailing ','
            newMethodCall = newMethodCall.substring(0, newMethodCall.length() - 1);
        }

        newMethodCall += ");";

        if(DEBUG_ENABLED)
            LoggingUtil.getLogger().fine("Injecting call to '" + newMethodCall + "' into target method");

        switch(plan.type()) {
            case INJECT_AFTER:
                newCtMethod.insertBefore("{ " + newMethodCall + " }");
                break;
            case INJECT_BEFORE:
                newCtMethod.insertAfter("{ " + newMethodCall + " }");
                break;
            case OVERWRITE:
                break;
            default:
                break;
        }

        if(DEBUG_ENABLED)
            LoggingUtil.getLogger().fine("Injected into method '" + newCtMethod.getName() + "'");

        targetCtClass.addMethod(newCtMethod);

        if(DEBUG_ENABLED)
            LoggingUtil.getLogger().fine("Added method '" + newCtMethod.getName() + "' to class '" + targetCtClass.getName() + "'");

        return targetCtClass;
    }
}
