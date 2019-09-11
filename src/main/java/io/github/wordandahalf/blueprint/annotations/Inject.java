package io.github.wordandahalf.blueprint.annotations;

import io.github.wordandahalf.blueprint.asm.bytecode.generation.BlueprintBytecodeGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Inject {
    String target();
    At at();
}