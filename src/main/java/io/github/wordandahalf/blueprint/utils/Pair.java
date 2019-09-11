package io.github.wordandahalf.blueprint.utils;

public class Pair<L, R> {
    public final L left;
    public final R right;

    private Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() { return this.left; }
    public R getRight() { return this.right; }

    public static<L, R> Pair of(L left, R right)  {
        return new Pair<L, R>(left, right);
    }
}
