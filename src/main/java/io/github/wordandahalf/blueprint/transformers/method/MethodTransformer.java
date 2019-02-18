package io.github.wordandahalf.blueprint.transformers.method;

import io.github.wordandahalf.blueprint.transformers.ClassTransformer;
import javassist.CtClass;
import javassist.CtMethod;

public abstract class MethodTransformer extends ClassTransformer {
    private String sourceMethodName, targetMethodName;

    public MethodTransformer(String sourceMethodName, String targetMethodName) {
        this.sourceMethodName = sourceMethodName;
        this.targetMethodName = targetMethodName;
    }

    @Override
    public CtClass apply(CtClass sourceClass, CtClass targetClass) throws Exception {
        CtMethod targetMethod = targetClass.getDeclaredMethod(this.targetMethodName);

        CtMethod newMethod = this.apply(
                sourceClass.getDeclaredMethod(this.sourceMethodName),
                targetClass.getDeclaredMethod(this.targetMethodName)
        );

        targetClass.removeMethod(targetMethod);
        targetClass.addMethod(newMethod);

        return targetClass;
    }

    public abstract CtMethod apply(CtMethod sourceMethod, CtMethod targetMethod) throws Exception;
}
