package io.github.wordandahalf.blueprint.classes.transformers.reference;

import io.github.wordandahalf.blueprint.logging.BlueprintLogger;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class FieldReferenceTransformer extends ReferenceTransformer {
    public FieldReferenceTransformer(String sourceClass, String sourceName, String targetClass, String targetName) {
        super(new ReferenceInfo(ReferenceInfo.ReferenceType.FIELD, sourceClass, sourceName,
                ReferenceInfo.ReferenceType.FIELD, targetClass, targetName));
    }

    public MethodNode apply(final MethodNode targetMethod, final ReferenceInfo info) {
        InsnList instructions = targetMethod.instructions;

        ListIterator<AbstractInsnNode> iterator = instructions.iterator();

        while(iterator.hasNext()) {
            AbstractInsnNode insnNode = iterator.next();

            if(insnNode instanceof FrameNode) {
                FrameNode frameNode = (FrameNode) insnNode;

                for(Object stack : frameNode.stack) {
                    BlueprintLogger.fine(FieldReferenceTransformer.class, "Stack: " + stack.toString());
                }

                for(Object var : frameNode.local) {
                    BlueprintLogger.fine(FieldReferenceTransformer.class, "Local: " + var.toString());
                }
            }

            if(insnNode instanceof FieldInsnNode) {
                FieldInsnNode fieldInsnNode = (FieldInsnNode) insnNode;

                if(fieldInsnNode.name.equals(this.info.getTargetName())) {
                    fieldInsnNode.owner = this.info.getTargetClass();
                }
            }
        }

        return targetMethod;
    }
}
