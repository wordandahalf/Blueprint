package io.github.wordandahalf.blueprint.transformers.method;

import io.github.wordandahalf.blueprint.exceptions.InvalidInjectionException;
import javassist.*;
import javassist.bytecode.*;

import java.util.Arrays;

public class MethodOverwriter extends MethodTransformer {
    public MethodOverwriter(String sourceMethod, String targetMethod) {
        super(sourceMethod, targetMethod);
    }

    private boolean validateOverwrite(CtMethod sourceMethod, CtMethod targetMethod) throws NotFoundException {
        // Rule 1
        if(!Modifier.isPrivate(sourceMethod.getModifiers())
                || !sourceMethod.getReturnType().getName().equals(targetMethod.getReturnType().getName())) {
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

    @Override
    public CtMethod apply(CtMethod sourceMethod, CtMethod targetMethod) throws Exception {
        if(!validateOverwrite(sourceMethod, targetMethod))
            throw new InvalidInjectionException(sourceMethod, targetMethod);

        CtMethod overwrittenMethod = targetMethod;

        MethodInfo sourceInfo = sourceMethod.getMethodInfo();
        MethodInfo targetInfo = overwrittenMethod.getMethodInfo();

        targetInfo.setCodeAttribute(sourceInfo.getCodeAttribute());

        return overwrittenMethod;
    }
}
