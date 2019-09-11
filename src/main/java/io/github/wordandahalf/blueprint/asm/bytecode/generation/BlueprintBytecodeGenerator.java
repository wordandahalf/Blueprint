package io.github.wordandahalf.blueprint.asm.bytecode.generation;

import io.github.wordandahalf.blueprint.asm.BlueprintContext;
import io.github.wordandahalf.blueprint.asm.bytecode.BlueprintBytecodeModification;

public abstract class BlueprintBytecodeGenerator implements Runnable {
    private BlueprintContext context;

    public BlueprintBytecodeGenerator(BlueprintContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        this.generate(this.context);
    }

    public abstract BlueprintBytecodeModification generate(BlueprintContext context);

    public BlueprintContext getContext() { return this.context; }
}
