package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotations.Blueprint;
import io.github.wordandahalf.blueprint.annotations.Plan;
import io.github.wordandahalf.blueprint.annotations.PlanType;

@Blueprint(target = "io.github.wordandahalf.blueprint.Foo")
public class BlueprintTest {
    public static void main(String[] args) throws Exception {
        Blueprints.add(BlueprintTest.class);

        Foo foo = new Foo();
        foo.getBar();
    }

    @Plan(method = "getBar", type = PlanType.INJECT_AFTER)
    public void getBar() {
        System.out.println("Hello from getBar()!");
    }
}
