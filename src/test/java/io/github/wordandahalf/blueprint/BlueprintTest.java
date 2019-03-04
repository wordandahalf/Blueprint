package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotations.At;
import io.github.wordandahalf.blueprint.annotations.Blueprint;
import io.github.wordandahalf.blueprint.annotations.Inject;
import io.github.wordandahalf.blueprint.annotations.Overwrite;
import io.github.wordandahalf.blueprint.logging.BlueprintLogger;

import java.util.logging.Level;

@Blueprint(target = "io.github.wordandahalf.blueprint.Foo")
public class BlueprintTest {
    public static void main(String[] args) throws Exception {
        BlueprintLogger.log(Level.INFO, BlueprintTest.class, "Starting BlueprintTest...");

        BlueprintLogger.log(Level.INFO, BlueprintTest.class, "Blueprints#add");
        Blueprints.add(BlueprintTest.class);

        BlueprintLogger.log(Level.INFO, BlueprintTest.class, "Blueprints#apply");
        Blueprints.apply();

        BlueprintLogger.log(Level.INFO, BlueprintTest.class, "Verify:");

        Foo foo = new Foo();

        BlueprintLogger.log(Level.INFO, BlueprintTest.class, "Foo#sayBar(\"Good morning\")");
        foo.sayBar("Good morning");

        BlueprintLogger.log(Level.INFO, BlueprintTest.class, "Foo#getFoo()");

        BlueprintLogger.log(Level.INFO, BlueprintTest.class, foo.getFoo());

        BlueprintLogger.log(Level.INFO, BlueprintTest.class, "Done with tests");
    }

    @Overwrite(target = "sayBar")
    public void sayBar(String greeting) {
        BlueprintLogger.log(Level.INFO, BlueprintTest.class, greeting + ", overwrite!");
    }

    @Inject(target = "getFoo", at = @At(location = At.Location.HEAD))
    private void getFoo_head() {
        BlueprintLogger.log(Level.INFO, BlueprintTest.class, "Entering getFoo()");
    }

    @Inject(target = "getFoo", at = @At(location = At.Location.TAIL))
    private void getFoo_tail() {
        BlueprintLogger.log(Level.INFO, BlueprintTest.class, "Leaving getFoo()");
    }
}
