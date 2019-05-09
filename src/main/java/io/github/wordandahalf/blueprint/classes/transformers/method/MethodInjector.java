package io.github.wordandahalf.blueprint.classes.transformers.method;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;

public class MethodInjector extends MethodTransformer {
    private MethodInjectionInfo info;

    public MethodInjector(MethodInjectionInfo info) {
        super(info.getSourceMethod(), info.getTargetMethod());
        this.info = info;
    }

    public MethodInjectionInfo getInjectionInfo() { return this.info; }

    @Override
    public MethodNode apply(final MethodNode sourceMethod, final MethodNode targetMethod) throws Exception {
        InsnList source = sourceMethod.instructions;
        InsnList target = targetMethod.instructions;

        InsnList copy = new InsnList();
        Iterator iterator = source.iterator();
        while(iterator.hasNext()) {
            AbstractInsnNode next = (AbstractInsnNode) iterator.next();

            int nextOpcode = next.getOpcode();

            if(
                nextOpcode != Opcodes.RETURN &&
                nextOpcode != Opcodes.DRETURN &&
                nextOpcode != Opcodes.FRETURN &&
                nextOpcode != Opcodes.IRETURN &&
                nextOpcode != Opcodes.LRETURN &&
                nextOpcode != Opcodes.ARETURN
            ) { copy.add(next); }
        }

        AbstractInsnNode tail = null;
        iterator = target.iterator();
        while(iterator.hasNext()) {
            AbstractInsnNode next = (AbstractInsnNode) iterator.next();

            int nextOpcode = next.getOpcode();

            if(
                nextOpcode == Opcodes.RETURN ||
                nextOpcode == Opcodes.DRETURN ||
                nextOpcode == Opcodes.FRETURN ||
                nextOpcode == Opcodes.IRETURN ||
                nextOpcode == Opcodes.LRETURN ||
                nextOpcode == Opcodes.ARETURN
            ) { tail = next; }
        }

        switch(this.info.getInjectionLocation().location()) {
            case HEAD:
                target.insert(copy);
                break;
            case TAIL:
                target.insertBefore(tail, copy);
                break;
            default:
                throw new UnsupportedOperationException("Injection location '" + this.info.getInjectionLocation().location() + " is not supported!");
        }

        return targetMethod;
    }
}
