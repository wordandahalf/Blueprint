package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotation.Blueprint;
import io.github.wordandahalf.blueprint.annotation.Overwrite;

@Blueprint(target = "io.github.wordandahalf.blueprint.Foo")
public class BlueprintTest {
    public static void main(String[] args) throws Exception {
        Blueprints.add(BlueprintTest.class);
        Blueprints.apply();

        Foo myFoo = new Foo();
        System.out.println(myFoo.getFoo());
        myFoo.sayBar(new String[] {"Hello, from String[]!"});
    }

    @Overwrite(target = "getFoo")
    private String getFoo() {
        return "Hello, from overwrite!";
    }
}
