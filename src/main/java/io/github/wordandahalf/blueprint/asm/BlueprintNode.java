package io.github.wordandahalf.blueprint.asm;

import java.security.InvalidParameterException;

/**
 * An object that contains the information needed to resolve a reference to, for example, a Blueprint's target or source.
 */
public class BlueprintNode {
    public enum Type {
        CLASS,
        METHOD,
        FIELD,
        ANNOTATION
    }

    private Type type;

    private String className;
    private String name;

    /**
     * ctor
     * @param type The type of target; if it is {@link Type}.CLASS, the second string parameter can be omitted
     * @param className The fully-qualified class name of the node
     * @param name If not of Type.CLASS, the name of the method, field, or annotation this node references
     */
    public BlueprintNode(Type type, String className, String... name) {
        this.type = type;

        if(type == Type.CLASS)
            this.className = className;
        else
        if(name.length != 1)
            throw new InvalidParameterException("BlueprintContext requires a name when the target type is not Type.CLASS!");
        else
            this.name = name[0];
    }

    /**
     * @return The type of node. See {@link Type}
     */
    public Type getType() { return this.type; }

    /**
     * @return The fully-qualified Java class name of the node
     */
    public String getClassName() { return this.className; }

    /**
     * @return If the type of this node is <b>not</b> {@link Type}.CLASS, this method will return the name of this node
     */
    public String getName() { return this.name; }
}
