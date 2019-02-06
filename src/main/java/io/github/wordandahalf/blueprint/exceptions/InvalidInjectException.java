package io.github.wordandahalf.blueprint.exceptions;

import javassist.CtMethod;

public class InvalidInjectException extends Exception {
    private CtMethod sourceMethod, targetMethod;

    public InvalidInjectException(CtMethod sourceMethod, CtMethod targetMethod) {
        this.sourceMethod = sourceMethod;
        this.targetMethod = targetMethod;
    }

    @Override
    public String getMessage() {
        return "The BlueprintAnnotationProcessor encountered an unexpected error when parsing injection method '" +
                sourceMethod.getLongName() + "' with the target '" + targetMethod.getLongName() + "'. Make sure that the injection method is a private, void, instance method with the same parameters as the target method.";
    }
}
