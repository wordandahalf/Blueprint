package io.github.wordandahalf.blueprint.transformers.method;

import io.github.wordandahalf.blueprint.transformers.ClassTransformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class MethodTransformer extends ClassTransformer {
    private String sourceMethodName, targetMethodName;

    public MethodTransformer(String sourceMethodName, String targetMethodName) {
        this.sourceMethodName = sourceMethodName;
        this.targetMethodName = targetMethodName;
    }

    public ClassNode apply(final ClassNode sourceClass, final ClassNode targetClass) throws Exception {
        MethodNode sourceMethod = null;
        MethodNode targetMethod = null;

        for(MethodNode methodNode : sourceClass.methods) {
            if(methodNode.name.equals(sourceMethodName)) {
                sourceMethod = methodNode;
            }
        }

        for(MethodNode methodNode : targetClass.methods) {
            if(methodNode.name.equals(targetMethodName)) {
                targetMethod = methodNode;
            }
        }

        MethodNode modifiedMethod = apply(sourceMethod, targetMethod);

        targetClass.methods.remove(targetMethod);
        targetClass.methods.add(modifiedMethod);

        return targetClass;
    }

    public abstract MethodNode apply(final MethodNode sourceMethod, final MethodNode targetMethod) throws Exception;
}
