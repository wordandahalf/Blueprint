package io.github.wordandahalf.blueprint;

public class Foo {
    private String text;

    public Foo() { this.text = "Hello, foo!"; }

    public static void staticMethod() { System.out.println("Hello, static world!"); }

    public void getBar() { System.out.println("BAR"); }
    public void getFoo(int i) { System.out.println(this.text); }
}
