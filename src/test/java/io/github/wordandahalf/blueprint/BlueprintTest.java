package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotations.At;
import io.github.wordandahalf.blueprint.annotations.Blueprint;
import io.github.wordandahalf.blueprint.annotations.Inject;
import io.github.wordandahalf.blueprint.annotations.Overwrite;

@Blueprint(target = "io.github.wordandahalf.blueprint.Foo")
public class BlueprintTest {
    public static void main(String[] args) throws Exception {
        Blueprints.add(BlueprintTest.class);

        Foo foo = new Foo();
        foo.getFoo(1, false);
        foo.getBar();
    }

    @Inject(target = "getFoo", at = @At(location = "HEAD"))
    public void getFoo(int i, boolean j) {
        i = 420;
        System.out.println("Hello from getBar()!");
    }

    @Overwrite(target = "getBar")
    public void getBar() {
        System.out.println("BAR OVERWRITE!");
    }
}
