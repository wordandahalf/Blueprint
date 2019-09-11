package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotations.At;
import io.github.wordandahalf.blueprint.annotations.Blueprint;
import io.github.wordandahalf.blueprint.annotations.Inject;
import io.github.wordandahalf.blueprint.annotations.Overwrite;
import io.github.wordandahalf.blueprint.environment.BlueprintLogger;

/**
 * This is the builtin test of Blueprint features.
 * Don't invoke any of the methods here during runtime--it is intended for testing purposes only.
 */
@Blueprint(target = Foo.class)
final class BlueprintTest {
    @Inject(target = "<init>", at = @At())
    public void ctor() {
        System.out.println("Hello, world foo!");
    }

    @Overwrite(target = "sayBar")
    public void sayBar(String greeting) {
        System.out.println(greeting + ", overwrite!");
    }

    public static void main(String[] args) throws Exception {
        BlueprintLogger.info(BlueprintTest.class, "Starting tests...");

        try {
            Blueprints.add(BlueprintTest.class);
            Blueprints.apply();
        } catch(Exception e) {
            BlueprintLogger.severe(BlueprintTest.class, e.getClass().getSimpleName() + ": '" + e.getMessage() + "'");
        }

        Foo foo = new Foo();
        foo.sayBar("Hello");

        BlueprintLogger.info(BlueprintTest.class, "Done with tests!");
    }
}
