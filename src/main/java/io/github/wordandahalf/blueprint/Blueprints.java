package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotations.Blueprint;
import io.github.wordandahalf.blueprint.annotations.BlueprintAnnotationParser;

import java.security.InvalidParameterException;

/**
 * Main class for adding and handling Blueprints
    //TODO: write better desc here.
 */
public class Blueprints {
    public static final boolean DEBUG_MODE = true;

    private static ClassLoader classLoader = Blueprints.class.getClassLoader();

    /**
     * Adds the provided class as a Blueprint injection class.
     * The provided class must be decorated with a {@link io.github.wordandahalf.blueprint.annotations.Blueprint} annotation for any other annotations to be processed.
     * Adding the same injection class multiple times is ignored.
     * @param clazz
     */
    public static void add(Class<?> clazz) {
        Blueprint blueprint = clazz.getAnnotation(Blueprint.class);

        if(blueprint != null) {
            // TODO: Use proper logging system

            System.out.println("Parsing Blueprint class with name '" + clazz.getName() + "' and target '" + blueprint.target().getName() + "'");

            BlueprintAnnotationParser.parse(blueprint, clazz);
        } else {
            throw new InvalidParameterException("The provided class does not have a Blueprint annotation");
        }
    }
    
    public static void apply() {

    }
}
