package io.github.wordandahalf.blueprint.exceptions;

import io.github.wordandahalf.blueprint.annotations.Plan;

public class PlanSignatureException extends Exception {
    private Plan plan;
    private String methodName, expectedSignature;

    public PlanSignatureException(Plan plan, String methodName, String expectedSignature) {
        this.plan = plan;
        this.methodName = methodName;
        this.expectedSignature = expectedSignature;
    }

    @Override
    public String getMessage() {
        return "Expected signature '" + expectedSignature + "' at method " + methodName + " for PlanType " + plan.type();
    }
}
