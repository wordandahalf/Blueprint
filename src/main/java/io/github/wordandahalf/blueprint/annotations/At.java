package io.github.wordandahalf.blueprint.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface At {
    enum Location {
        HEAD,
        TAIL,
        LINE,
        INVOCATION,
        FIELD,
        NEW,
    }

    Location location() default Location.HEAD;

    /**
     * Any needed arguments for the location
     */
    String[] args() default {};
}