package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.transformers.ClassTransformer;
import io.github.wordandahalf.blueprint.utils.Pair;

import java.util.*;

public class BlueprintClassTransformerPool {
    private LinkedHashMap<Pair<String, String>, LinkedList<ClassTransformer>> pool
            = new LinkedHashMap<>();

    public void add(String sourceClassName, String targetClassName, List<ClassTransformer> transformers) {
        LinkedList<ClassTransformer> list;
        Pair<String, String> key = Pair.of(sourceClassName, targetClassName);
        if(pool.containsKey(key))
            list = pool.get(key);
        else
            list = new LinkedList<>();

        list.addAll(transformers);
        pool.put(key, list);
    }

    public void add(String sourceClassName, String targetClassName, ClassTransformer... transformers) {
        add(sourceClassName, targetClassName, Arrays.asList(transformers));
    }

    public void remove(String sourceClassName, String targetClassName, ClassTransformer transformer) {
        LinkedList<ClassTransformer> list;
        Pair<String, String> key = Pair.of(sourceClassName, targetClassName);
        if(pool.containsKey(key))
            list = pool.get(key);
        else
            return;

        list.remove(transformer);
        pool.put(key, list);
    }

    public List<ClassTransformer> getTransformerByPair(Pair<String, String> classPair) {
        return pool.get(classPair);
    }

    public List<ClassTransformer> getTransformersBySource(String sourceClassName) {
        ArrayList<ClassTransformer> list = new ArrayList<>();

        for(Pair<String, String> pair : pool.keySet()) {
            if(pair.left.equals(sourceClassName)) {
                list.addAll(pool.get(pair));
            }
        }

        return list;
    }

    public List<ClassTransformer> getTransformersByTarget(String targetClassName) {
        ArrayList<ClassTransformer> list = new ArrayList<>();

        for(Pair<String, String> pair : pool.keySet()) {
            if(pair.right.equals(targetClassName)) {
                list.addAll(pool.get(pair));
            }
        }

        return list;
    }

    public Set<Pair<String, String>> getClassPairs() { return pool.keySet(); }

    public List<String> getSourceClasses() {
        ArrayList<String> list = new ArrayList<>(pool.keySet().size());

        pool.keySet().forEach(pair -> {
            String className = pair.left;

            if(!list.contains(className))
                list.add(pair.right);
        });

        return list;
    }

    public List<String> getTargetClasses() {
        ArrayList<String> list = new ArrayList<>(pool.keySet().size());

        pool.keySet().forEach(pair -> {
            String className = pair.right;

            if(!list.contains(className))
                list.add(pair.right);
        });

        return list;
    }
}
