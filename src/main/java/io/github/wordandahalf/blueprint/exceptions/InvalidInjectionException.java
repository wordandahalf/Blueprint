package io.github.wordandahalf.blueprint.exceptions;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import java.util.Arrays;

public class InvalidInjectionException extends Exception {
    private CtMethod sourceMethod, targetMethod;

    public InvalidInjectionException(CtMethod sourceMethod, CtMethod targetMethod) {
        this.sourceMethod = sourceMethod;
        this.targetMethod = targetMethod;
    }

    private String getReason() throws NotFoundException {
        if(!Modifier.isPrivate(sourceMethod.getModifiers()))
            return "the source method is not private!";

        if(!sourceMethod.getReturnType().getName().equals(CtClass.voidType.getName()))
            return "the source method is not void!";

        if(Modifier.isStatic(targetMethod.getModifiers()) && !Modifier.isStatic(sourceMethod.getModifiers()))
            return "the source method is not static!";

        if(!Modifier.isStatic(targetMethod.getModifiers()) && Modifier.isStatic(sourceMethod.getModifiers()))
            return "the source method is not an instance method!";

        if(!Arrays.equals(sourceMethod.getParameterTypes(), targetMethod.getParameterTypes()))
            return "the source method does not have the same parameter types!";

        return "404: Error message not found. Please report this to the Blueprint repository with adequate code snippets!";
    }

    @Override
    public String getMessage() {
        try {
            return "The was an error when parsing injection method '" +
                    sourceMethod.getLongName() + "' with the target '" + targetMethod.getLongName() + "' because " + getReason();
        } catch(NotFoundException e) {
            return "A NotFoundException was thrown when trying to generate an InvalidInjectException! Please report this to the Blueprint repository with adequate code snippets!";
        }
    }
}
