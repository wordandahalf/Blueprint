package io.github.wordandahalf.blueprint.asm.bytecode;

import io.github.wordandahalf.blueprint.asm.BlueprintNode;
import io.github.wordandahalf.blueprint.environment.BlueprintClassManager;
import org.objectweb.asm.tree.InsnList;

import java.util.Objects;

public class BlueprintBytecode {
    private BlueprintNode source;
    private InsnList bytecode;

    private boolean frozen = false;

    public BlueprintBytecode(BlueprintNode source) {
        this.source = source;

        // TODO: Maybe think about this more:
        if(source.getType() != BlueprintNode.Type.METHOD) {
            throw new UnsupportedOperationException("BlueprintBytecode can only be created from a BlueprintNode with type METHOD!");
        }

        try {
            Objects.requireNonNull(BlueprintClassManager.get(source.getClassName())).methods.forEach(
                (methodNode) -> {
                    if(methodNode.name.equals(source.getName())) {
                        this.bytecode = methodNode.instructions;
                    }
                }
            );
        } catch(NullPointerException e) {
            // TODO: Proper system for finding annotations
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public BlueprintBytecode(BlueprintNode source, InsnList bytecode) {
        this.source = source;
        this.bytecode = bytecode;
    }

    public void modify(BlueprintBytecodeModification modification) {
        if(frozen)
            throw new IllegalStateException("Tried modifying a BlueprintBytecode after it was applied to a class!");

        // TODO
    }

    public void apply() {
        if(frozen)
            throw new IllegalStateException("Tried applying a BlueprintBytecode after it was applied to a class!");

        Objects.requireNonNull(BlueprintClassManager.get(source.getClassName())).methods.forEach(
                (methodNode) -> {
                    if(methodNode.name.equals(source.getName())) {
                        methodNode.instructions.clear();
                        methodNode.instructions.add(this.getBytecode());
                    }
                }
        );

        this.frozen = true;
    }

    public BlueprintNode getSource() { return this.source; }
    public InsnList getBytecode() { return this.bytecode; }
}
