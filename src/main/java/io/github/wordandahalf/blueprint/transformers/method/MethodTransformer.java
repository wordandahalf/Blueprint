package io.github.wordandahalf.blueprint.transformers.method;

import io.github.wordandahalf.blueprint.classes.BlueprintClass;
import io.github.wordandahalf.blueprint.transformers.ClassTransformer;
import org.objectweb.asm.tree.ClassNode;
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

        ClassNode sourceNode = sourceClass.getClassNode();
        ClassNode targetNode = targetClass.getClassNode();

        for(MethodNode methodNode : sourceNode.methods) {
            if(methodNode.name.equals(sourceMethodName)) {
                sourceMethod = methodNode;
            }
        }

        for(MethodNode methodNode : targetNode.methods) {
            if(methodNode.name.equals(targetMethodName)) {
                targetMethod = methodNode;
            }
        }

        MethodNode modifiedMethod = apply(sourceMethod, targetMethod);

        targetNode.methods.remove(targetMethod);
        targetNode.methods.add(modifiedMethod);

        return targetClass;
    }

    public abstract MethodNode apply(final MethodNode sourceMethod, final MethodNode targetMethod) throws Exception;
}
