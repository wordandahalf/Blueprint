package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotations.*;
import io.github.wordandahalf.blueprint.logging.BlueprintLogger;

import java.util.logging.Level;

@Blueprint(target = "io.github.wordandahalf.blueprint.Foo")
public abstract class BlueprintTest {
    @Shadow
    abstract String getFoo();

    @Shadow
    private String fooText;

    public static void main(String[] args) {
        try {
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
        catch (Exception e) {
            BlueprintLogger.severe(BlueprintTest.class, e.getClass().getSimpleName() + ": " + e.getMessage());

            for (StackTraceElement element : e.getStackTrace()) {
                BlueprintLogger.severe(BlueprintTest.class, element.toString());
            }
        }
    }

    @Overwrite(target = "sayBar")
    public void sayBar(String greeting) {
        System.out.println(greeting + ", overwrite!");

        System.out.println("Setting fooText to 'Hello, foo injection!'");

        this.fooText = "Hello, foo injection!";

        System.out.println("getFoo(): " + this.getFoo());
    }

    @Inject(target = "getFoo", at = @At(location = At.Location.HEAD))
    private void getFoo_head() {
        System.out.println("Entering getFoo()");
    }

    @Inject(target = "getFoo", at = @At(location = At.Location.TAIL))
    private void getFoo_tail() {
        System.out.println("Leaving getFoo()");
    }
}
