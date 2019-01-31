package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.annotations.Blueprint;
import io.github.wordandahalf.blueprint.annotations.Plan;
import io.github.wordandahalf.blueprint.annotations.PlanType;

@Blueprint(target = "io.github.wordandahalf.blueprint.Foo")
public class BlueprintTest {
    public static void main(String[] args) throws Exception {
        Blueprints.add(BlueprintTest.class);

        Foo foo = new Foo();
        foo.getFoo(1, false);
        foo.getBar();
    }

    @Plan(method = "getFoo", type = PlanType.INJECT_BEFORE)
    public void getFoo(int i, boolean j) {
        i = 420;
        System.out.println("Hello from getBar()!");
    }

    @Plan(method = "getBar", type = PlanType.OVERWRITE)
    public void getBar() {
        System.out.println("BAR OVERWRITE!");
    }
}
