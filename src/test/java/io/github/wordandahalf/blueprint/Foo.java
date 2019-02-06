package io.github.wordandahalf.blueprint;

public class Foo {
    private String fooText;

    public Foo() {
        this.fooText = "Hello, foo!";
    }

    public String getFoo() { return this.fooText; }
    public void sayBar() { System.out.println("Hello, bar!"); }
}
