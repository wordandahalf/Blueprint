package io.github.wordandahalf.blueprint.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BlueprintMethodVisitor extends MethodVisitor {
    public BlueprintMethodVisitor() {
        super(Opcodes.ASM7);
    }
}
