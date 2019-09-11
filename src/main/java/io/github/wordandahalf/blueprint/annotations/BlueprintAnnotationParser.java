package io.github.wordandahalf.blueprint.annotations;

import io.github.wordandahalf.blueprint.asm.BlueprintContext;
import io.github.wordandahalf.blueprint.asm.BlueprintNode;
import io.github.wordandahalf.blueprint.asm.bytecode.generation.BlueprintInjectGenerator;
import io.github.wordandahalf.blueprint.environment.BlueprintManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BlueprintAnnotationParser {
    public static void parse(Blueprint blueprint, Class<?> clazz) {
        BlueprintContext context = null;

        // Make sure that the Blueprint doesn't target itself, a Java system class, or a Blueprint class (except the dedicated testing class)
        if(blueprint.target().getName().equals(clazz.getName())
            || blueprint.target().getName().startsWith("java.")
            || (blueprint.target().getName().startsWith("io.github.wordandahalf.blueprint.")
                && !blueprint.target().getName().equals("io.github.wordandahalf.blueprint.Foo"))) {
            throw new UnsupportedOperationException("Blueprint has an invalid target!");
        }

        for(Method m : clazz.getDeclaredMethods()) {
            // TODO: Proper system for finding annotations

            Inject inject = m.getAnnotation(Inject.class);

            if(inject != null) {
                // TODO: Use proper logging system
                System.out.println("Found inject on method " + m.getName());

                BlueprintNode source = new BlueprintNode(BlueprintNode.Type.METHOD, clazz.getName(), m.getName());
                BlueprintNode target = new BlueprintNode(BlueprintNode.Type.METHOD, blueprint.target().getName(), inject.target());

                context = new BlueprintContext(source, target);
            }

            Overwrite overwrite = m.getAnnotation(Overwrite.class);

            if(overwrite != null) {
                // TODO: Use proper logging system
                System.out.println("Found overwrite on method " + m.getName());
            }
        }

        for(Field f : clazz.getDeclaredFields()) {
            // TODO: Finish field annotations
        }

        if(context != null)
            BlueprintManager.add(context);
    }
}
