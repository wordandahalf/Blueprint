package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotations.At;
import io.github.wordandahalf.blueprint.annotations.Blueprint;
import io.github.wordandahalf.blueprint.annotations.Inject;

@Blueprint(target = "io.github.wordandahalf.blueprint.Foo")
public class BlueprintTest {
    public static void main(String[] args) throws Exception {
        Blueprints.add(BlueprintTest.class);

        Foo myFoo = new Foo();
        System.out.println(myFoo.getFoo());
        myFoo.sayBar();
    }

    @Inject(target = "getFoo", at = @At(location = At.HEAD))
    private void getFoo() {
        System.out.println("Hello, from injection!");
    }
}
