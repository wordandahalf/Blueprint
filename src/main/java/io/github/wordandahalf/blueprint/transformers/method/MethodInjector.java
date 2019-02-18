package io.github.wordandahalf.blueprint.transformers.method;

import javassist.CtMethod;

public class MethodInjector extends MethodTransformer {
    private MethodInjectionInfo info;

    public MethodInjector(MethodInjectionInfo info) {
        super(info.getSourceMethod(), info.getTargetMethod());
        this.info = info;
    }

    public MethodInjectionInfo getInjectionInfo() { return this.info; }

    @Override
    public javassist.CtMethod apply(CtMethod sourceMethod, CtMethod targetMethod) {
        return null;
    }
}
