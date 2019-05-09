package io.github.wordandahalf.blueprint.utils;

import io.github.wordandahalf.blueprint.classes.transformers.ClassTransformer;
import io.github.wordandahalf.blueprint.classes.transformers.method.MethodTransformer;
import io.github.wordandahalf.blueprint.classes.transformers.reference.ReferenceTransformer;

import java.util.Comparator;

public class ClassTransformerComparator implements Comparator<ClassTransformer> {
    private int getPriority(ClassTransformer transformer) {
        // Priority (from greatest to least):
        // ReferenceTransformer
        // MethodTransformer

        if(transformer instanceof ReferenceTransformer) {
            return 0;
        } else
        if(transformer instanceof MethodTransformer) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int compare(ClassTransformer transformer1, ClassTransformer transformer2) {
        int comparativePriority = getPriority(transformer1) - getPriority(transformer2);

        if(comparativePriority > 0)
            return 1;
        if(comparativePriority == 0)
            return 0;


        return -1;
    }
}
