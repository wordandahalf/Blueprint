package io.github.wordandahalf.blueprint.injection;

import io.github.wordandahalf.blueprint.exceptions.PlanSignatureException;
import javassist.*;

public class InjectionHelper {
    /**
     * Indicates where the injection should take place in relation to the original function
     */
    public enum InjectionLocation  {
        INJECT_BEFORE(),
        INJECT_AFTER();
    }

    /**
     * Overwrites a targeted method
     * To be more specific, this renames the targeted method to a unique name,
     * copies the source method to the target method's class and renames it to ensure it
     * is the same as the target method
     * @param targetMethod
     * @param sourceMethod
     * @return The edited class for loading
     * @throws PlanSignatureException If the signature of the source method is not exactly the same as the target method
     * @throws CannotCompileException (This should never be thrown) If the source method's code cannot be compiled
     */
    public static CtClass overwriteMethod(CtMethod sourceMethod, CtMethod targetMethod) throws PlanSignatureException, CannotCompileException {
        if(!targetMethod.getSignature().equals(sourceMethod.getSignature()))
            throw new PlanSignatureException(targetMethod.getName(), targetMethod.getSignature());

        CtClass targetClass = targetMethod.getDeclaringClass();

        //Generate a new unique method name
        String newTargetMethodName = "blueprint$" + targetMethod.getName() + "_" + System.currentTimeMillis();

        //Add the source method to the target class, ensuring it has the same name as the target method
        CtMethod injectedMethod = CtNewMethod.copy(sourceMethod, targetMethod.getName(),
                targetClass, null);

        targetMethod.setName(newTargetMethodName);

        targetClass.addMethod(injectedMethod);

        return targetClass;
    }

    /**
     * Injects code into an existing method
     * To be more specific, this renames the targeted method to a unique name,
     * copies the source method to the target method's class and renames it, and then injects a call to the renamed method in it.
     * @param targetMethod The method to "inject" into
     * @param sourceMethod The method to inject
     * @param location Where the code should be injected in relation to the original function
     * @return The edited class for loading
     * @throws CannotCompileException
     */
    public static CtClass injectMethod(CtMethod sourceMethod, CtMethod targetMethod, InjectionLocation location) throws PlanSignatureException, CannotCompileException, NotFoundException {
        if(!targetMethod.getSignature().equals(sourceMethod.getSignature()))
            throw new PlanSignatureException(targetMethod.getName(), targetMethod.getSignature());

        CtClass targetClass = targetMethod.getDeclaringClass();

        //Generate a new unique method name
        String newTargetMethodName = "blueprint$" + targetMethod.getName() + "_" + System.currentTimeMillis();

        //Add the source method to the target class, ensuring it has the same name as the target method
        CtMethod injectedMethod = CtNewMethod.copy(sourceMethod, targetMethod.getName(),
                                            targetClass, null);

        targetMethod.setName(newTargetMethodName);

        String targetMethodCall = newTargetMethodName + "(";

        int numberOfParameters = targetMethod.getParameterTypes().length;

        if(numberOfParameters > 0) {
            for (int i = 0; i < numberOfParameters; i++) {
                //$0 is the class instance (the 'this' keyword) -- we don't want that
                targetMethodCall += "$" + (i + 1) + ",";
            }

            //To get rid of the trailing ','
            targetMethodCall = targetMethodCall.substring(0, targetMethodCall.length() - 1);
        }

        //Add the closing parentheses and semicolon
        targetMethodCall += ");";

        switch(location) {
            case INJECT_BEFORE:
                injectedMethod.insertAfter(targetMethodCall);
                break;
            case INJECT_AFTER:
                injectedMethod.insertBefore(targetMethodCall);
                break;
            default:
                break;
        }

        targetClass.addMethod(injectedMethod);

        return targetClass;
    }
}
