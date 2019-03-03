package io.github.wordandahalf.blueprint.transformers.method;

import io.github.wordandahalf.blueprint.annotations.At;

public class MethodInjectionInfo {
    private String sourceMethodName, targetMethodName;
    private At location;

    public MethodInjectionInfo(String sourceMethodName, String targetMethodName, At location) {
        this.sourceMethodName = sourceMethodName;
        this.targetMethodName = targetMethodName;

        this.location = location;
    }

    public String getSourceMethod() { return this.sourceMethodName; }
    public String getTargetMethod() { return this.targetMethodName; }

    public At getInjectionLocation() { return this.location; }
}
