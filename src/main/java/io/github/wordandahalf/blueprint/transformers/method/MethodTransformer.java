package io.github.wordandahalf.blueprint.transformers.method;

import io.github.wordandahalf.blueprint.classes.BlueprintClass;
import io.github.wordandahalf.blueprint.transformers.ClassTransformer;
import org.objectweb.asm.tree.MethodNode;

public abstract class MethodTransformer extends ClassTransformer {
    private String sourceMethodName, targetMethodName;

    public MethodTransformer(String sourceMethodName, String targetMethodName) {
        this.sourceMethodName = sourceMethodName;
        this.targetMethodName = targetMethodName;
    }

    public BlueprintClass apply(final BlueprintClass sourceClass, final BlueprintClass targetClass) throws Exception {
        MethodNode sourceMethod = null;
        MethodNode targetMethod = null;

        for(MethodNode methodNode : sourceClass.getClassNode().methods) {
            if(methodNode.name.equals(sourceMethodName)) {
                sourceMethod = methodNode;
            }
        }

        for(MethodNode methodNode : targetClass.getClassNode().methods) {
            if(methodNode.name.equals(targetMethodName)) {
                targetMethod = methodNode;
            }
        }

        MethodNode modifiedMethod = apply(sourceMethod, targetMethod);

        targetClass.getClassNode().methods.remove(targetMethod);
        targetClass.getClassNode().methods.add(modifiedMethod);

        return targetClass;
    }

    public abstract MethodNode apply(final MethodNode sourceMethod, final MethodNode targetMethod) throws Exception;
}
