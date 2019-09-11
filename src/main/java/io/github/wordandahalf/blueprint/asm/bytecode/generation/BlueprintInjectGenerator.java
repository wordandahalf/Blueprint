package io.github.wordandahalf.blueprint.asm.bytecode.generation;

import io.github.wordandahalf.blueprint.asm.BlueprintContext;
import io.github.wordandahalf.blueprint.asm.bytecode.BlueprintBytecode;
import io.github.wordandahalf.blueprint.asm.bytecode.BlueprintBytecodeModification;

public class BlueprintInjectGenerator extends BlueprintBytecodeGenerator {
    public BlueprintInjectGenerator(BlueprintContext context) {
        super(context);
    }

    public BlueprintBytecodeModification generate(BlueprintContext context) {
        BlueprintBytecode source = new BlueprintBytecode(context.getSource());
        BlueprintBytecode target = new BlueprintBytecode(context.getTarget());

        return new BlueprintBytecodeModification();
    }
}
