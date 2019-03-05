package io.github.wordandahalf.blueprint.transformers;

import org.objectweb.asm.tree.ClassNode;

public abstract class ClassTransformer {
    public abstract ClassNode apply(final ClassNode sourceClass, final ClassNode targetClass) throws Exception;
}
