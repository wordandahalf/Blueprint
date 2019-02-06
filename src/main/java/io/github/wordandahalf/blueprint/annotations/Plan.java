package io.github.wordandahalf.blueprint.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to denote methods that are to be injected into a class provided by its class's {@link Blueprint} annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Plan {
    String method();
    PlanType type();
}
