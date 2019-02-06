package io.github.wordandahalf.blueprint.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface At {
    String  HEAD = "HEAD",
            TAIL = "TAIL",
            BEFORE_RETURN = "BEFORE_RETURN",
            INVOCATION = "INVOCATION";

    /**
     * The location for a provided injection
     * Current supported locations include:
     * HEAD (The beginning of the method)
     * TAIL (The end of the method)
     * BEFORE_RETURN (Before the return of a method)
     * INVOCATION (Replaces the invocation of a specified method, see {@link At#args()})
     */
    String location();

    /**
     * Any needed arguments for the location argument.
     */
    String[] args() default {};
}
