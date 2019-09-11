package io.github.wordandahalf.blueprint.asm;

import io.github.wordandahalf.blueprint.asm.bytecode.BlueprintBytecodeModification;
import io.github.wordandahalf.blueprint.asm.bytecode.generation.BlueprintBytecodeGenerator;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * An object containing info for Blueprints
 */
public class BlueprintContext {
    private BlueprintNode source, target;

    private List<BlueprintBytecodeGenerator> generators;
    private List<BlueprintBytecodeModification> modifications;

    public BlueprintContext(BlueprintNode source, BlueprintNode target) {
        this.source = source;
        this.target = target;

        this.generators = new ArrayList<>();
        this.modifications = new ArrayList<>();
    }

    public void addGenerator(BlueprintBytecodeGenerator generator)
    {
        this.generators.add(generator);
    }

    public void generateBytecode()
    {
        for(BlueprintBytecodeGenerator generator : generators)
        {
            this.generators.remove(generator);
            this.modifications.add(generator.generate(this));
        }
    }

    public void applyModifications()
    {
        for(BlueprintBytecodeModification modification : modifications)
        {
            this.modifications.remove(modification);
            modification.apply();
        }
    }

    public BlueprintNode getSource() { return this.source; }
    public BlueprintNode getTarget() { return this.target; }

    /**
     * @return A {@link List} of all parsed Blueprint annotations that have yet been interpreted into a {@link BlueprintBytecodeModification}
     */
    public List<BlueprintBytecodeGenerator> getGenerators() { return this.generators; }

    /**
     * @return A {@link List} of all {@link BlueprintBytecodeModification} that can be applied to its target class
     */
    public List<BlueprintBytecodeModification> getModifications() { return this.modifications; }
}
