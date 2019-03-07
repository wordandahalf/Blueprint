package io.github.wordandahalf.blueprint.transformers;

import io.github.wordandahalf.blueprint.classes.BlueprintClass;

public abstract class ClassTransformer {
    public abstract BlueprintClass apply(final BlueprintClass sourceClass, final BlueprintClass targetClass) throws Exception;
}
