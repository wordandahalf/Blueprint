package io.github.wordandahalf.blueprint.transformers.method;

import org.objectweb.asm.tree.MethodNode;

public class MethodOverwriter extends MethodTransformer {
    public MethodOverwriter(String sourceMethod, String targetMethod) {
        super(sourceMethod, targetMethod);
    }

    @Override
    public MethodNode apply(MethodNode sourceMethod, MethodNode targetMethod) throws Exception {
        targetMethod.instructions = sourceMethod.instructions;

        return targetMethod;
    }
}
