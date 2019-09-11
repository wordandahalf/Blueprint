package io.github.wordandahalf.blueprint.classes.transformers.reference;

import io.github.wordandahalf.blueprint.classes.BlueprintClassPrinter;
import io.github.wordandahalf.blueprint.logging.BlueprintLogger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class MethodReferenceTransformer extends ReferenceTransformer {
    public MethodReferenceTransformer(String sourceClass, String sourceName, String targetClass, String targetName) {
        super(new ReferenceInfo(ReferenceInfo.ReferenceType.METHOD, sourceClass, sourceName,
                ReferenceInfo.ReferenceType.METHOD, targetClass, targetName));
    }

    public MethodNode apply(final MethodNode targetMethod, final ReferenceInfo info) {
        InsnList instructions = targetMethod.instructions;

        if(targetMethod.localVariables != null) {
            for(LocalVariableNode variable : targetMethod.localVariables) {
                // Descriptions are of the form "L<slash-delimited path to class>;"
                if(variable.name.equals("this")) {
                    String oldName = "L" + this.info.getTargetClass().replace(".", "/") + ";";

                    if(variable.desc.equals(oldName)) {
                        String newName = "L" + this.info.getSourceClass().replace(".", "/") + ";";

                        BlueprintLogger.finest(MethodReferenceTransformer.class, "Replacing '" + oldName + "' with '" + newName);
                    }
                }
            }
        }

        ListIterator<AbstractInsnNode> iterator = instructions.iterator();

        while(iterator.hasNext()) {
            AbstractInsnNode insnNode = iterator.next();

            if(insnNode instanceof FrameNode) {
                FrameNode frameNode = (FrameNode) insnNode;

                for(Object stack : frameNode.stack) {
                    BlueprintLogger.fine(MethodReferenceTransformer.class, "Stack: " + stack.toString());
                }

                for(Object var : frameNode.local) {
                    BlueprintLogger.fine(MethodReferenceTransformer.class, "Local: " + var.toString());
                }
            }

            if(insnNode instanceof MethodInsnNode) {
                MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;

                if(methodInsnNode.name.equals(this.info.getTargetName())) {
                    System.out.println("Remapping " + methodInsnNode.getOpcode() + ": ");
                    System.out.println("(" + methodInsnNode.desc + ") " + methodInsnNode.owner + "/" + methodInsnNode.name);

                    methodInsnNode.name = this.info.getSourceName();
                    methodInsnNode.owner = this.info.getSourceClass();

                    System.out.println("New:");
                    System.out.println("(" + methodInsnNode.desc + ") " + methodInsnNode.owner + "/" + methodInsnNode.name);
                }
            }
        }

        return targetMethod;
    }
}
