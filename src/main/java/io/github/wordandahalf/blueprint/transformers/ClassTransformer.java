package io.github.wordandahalf.blueprint.transformers;

import javassist.CtClass;

public abstract class ClassTransformer {
    public abstract CtClass apply(CtClass sourceClass, CtClass targetClass) throws Exception;
}
