package io.github.wordandahalf.blueprint;

import java.util.Random;

public class Foo {
    private String fooText;

    public Foo() {
        this.fooText = "Hello, foo!";
    }

    public String getFoo() {
        int[] dummyArray = new int[69];
        Random random = new Random(new Random().nextLong());

        for(int i = 0; i < dummyArray.length; i++) {
            dummyArray[i] = random.nextInt();
        }

        dummyArray[0] = random.nextInt();
        dummyArray[dummyArray.length / 2] = random.nextInt();

        return this.fooText;
    }
    public void sayBar(String[] args) {
        System.out.println("Hello, bar!");

        for(String str : args) {
            System.out.println(str);
        }
    }
}
