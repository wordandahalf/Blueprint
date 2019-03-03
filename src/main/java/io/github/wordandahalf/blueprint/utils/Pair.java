package io.github.wordandahalf.blueprint.utils;

public class Pair<L, R> {
    public L left;
    public R right;

    private Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() { return this.left; }
    public R getRight() { return this.right; }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Pair))
            return false;

        Pair<?, ?> otherPair = (Pair<?, ?>) obj;

        return (otherPair.left.equals(this.left)) &&
                (otherPair.right.equals(this.right));
    }

    public static <L, R> Pair of(L left, R right) {
        return new Pair<>(left, right);
    }
}
