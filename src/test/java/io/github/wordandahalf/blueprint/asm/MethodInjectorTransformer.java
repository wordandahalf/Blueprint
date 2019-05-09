package io.github.wordandahalf.blueprint.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class MethodInjectorTransformer extends ClassVisitor {
    public MethodInjectorTransformer() {
        super(Opcodes.ASM7);
    }
}
