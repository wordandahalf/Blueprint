package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotations.At;
import io.github.wordandahalf.blueprint.annotations.Blueprint;
import io.github.wordandahalf.blueprint.annotations.Inject;
import io.github.wordandahalf.blueprint.annotations.Overwrite;

@Blueprint(target = "io.github.wordandahalf.blueprint.Foo")
public class BlueprintTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting BlueprintTest...");

        System.out.println("Blueprints#add");
        Blueprints.add(BlueprintTest.class);

        System.out.println("Blueprints#apply");
        Blueprints.apply();

        System.out.println("Verify:");

        Foo foo = new Foo();

        System.out.println("Foo#sayBar(\"Good morning\")");
        foo.sayBar("Good morning");

        System.out.println("Foo#getFoo()");

        System.out.println(foo.getFoo());

        System.out.println("Done with tests");
    }

    @Overwrite(target = "sayBar")
    public void sayBar(String greeting) {
        System.out.println(greeting + ", overwrite!");
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
