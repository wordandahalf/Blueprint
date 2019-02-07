package io.github.wordandahalf.blueprint.injection;

import io.github.wordandahalf.blueprint.Blueprints;
import io.github.wordandahalf.blueprint.exceptions.InvalidInjectException;
import io.github.wordandahalf.blueprint.utils.LoggingUtil;
import javassist.*;
import javassist.bytecode.*;
import sun.rmi.runtime.Log;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;

public class InjectionHelper {
    /**
     * Indicates where the injection should take place in relation to the original function
     */
    public enum InjectionLocation  {
        INJECT_BEFORE(),
        INJECT_AFTER()
    }

    /**
     * Overwrites a targeted method
     * To be more specific, this renames the targeted method to a unique name,
     * copies the source method to the target method's class and renames it to ensure it
     * is the same as the target method
     * @param targetMethod
     * @param sourceMethod
     * @return The edited class for loading
     * @throws CannotCompileException (This should never be thrown) If the source method's code cannot be compiled
     */
    public static CtClass overwriteMethod(CtMethod sourceMethod, CtMethod targetMethod) throws NotFoundException, InvalidInjectException, CannotCompileException {
        if(!validateInjectionMethod(sourceMethod, targetMethod))
            throw new InvalidInjectException(sourceMethod, targetMethod);

        CtClass targetClass = targetMethod.getDeclaringClass();

        CtMethod overwrittenMethod = targetMethod;

        MethodInfo sourceInfo = sourceMethod.getMethodInfo();
        MethodInfo targetInfo = overwrittenMethod.getMethodInfo();

        targetClass.removeMethod(targetMethod);

        targetInfo.setCodeAttribute(sourceInfo.getCodeAttribute());

        targetClass.addMethod(overwrittenMethod);

        return targetClass;
    }

    private static boolean validateInjectionMethod(CtMethod sourceMethod, CtMethod targetMethod) throws NotFoundException {
        // Rule 1
        if(!Modifier.isPrivate(sourceMethod.getModifiers())
            || !sourceMethod.getReturnType().getName().equals(CtClass.voidType.getName())) {
            return false;
        }

        // Rule 2
        if((Modifier.isStatic(targetMethod.getModifiers()) && !Modifier.isStatic(sourceMethod.getModifiers()))
            || (!Modifier.isStatic(targetMethod.getModifiers()) && (Modifier.isStatic(sourceMethod.getModifiers())))) {
            return false;
        }

        //Rule 3
        if(!Arrays.equals(sourceMethod.getParameterTypes(), targetMethod.getParameterTypes())) {
            return false;
        }

        return true;
    }

    public static CtClass injectMethod(CtMethod sourceMethod, CtMethod targetMethod, InjectionLocation location) throws NotFoundException, InvalidInjectException, CannotCompileException {
        if(!validateInjectionMethod(sourceMethod, targetMethod))
            throw new InvalidInjectException(sourceMethod, targetMethod);

        CtClass targetClass = targetMethod.getDeclaringClass();

        CtMethod injectedMethod = targetMethod;

        MethodInfo sourceInfo = sourceMethod.getMethodInfo();
        MethodInfo targetInfo = injectedMethod.getMethodInfo();

        targetClass.removeMethod(targetMethod);

        switch(location) {
            case INJECT_AFTER:
                targetInfo.setCodeAttribute(
                        BytecodeHelper.inject(
                                sourceInfo,
                                targetInfo,
                                targetInfo.getCodeAttribute().getCodeLength() - 1
                        )
                );
                break;
            case INJECT_BEFORE:
                targetInfo.setCodeAttribute(
                        BytecodeHelper.inject(
                                sourceInfo,
                                targetInfo, 0
                        )
                );
                break;
            default:
                break;
        }

        targetClass.addMethod(injectedMethod);

        return targetClass;
    }
}
