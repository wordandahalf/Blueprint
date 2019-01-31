package io.github.wordandahalf.blueprint.exceptions;

public class PlanSignatureException extends Exception {
    private String methodName, expectedSignature;

    public PlanSignatureException(String methodName, String expectedSignature) {
        this.methodName = methodName;
        this.expectedSignature = expectedSignature;
    }

    @Override
    public String getMessage() {
        return "Expected signature '" + expectedSignature + "' at method " + methodName;
    }
}
