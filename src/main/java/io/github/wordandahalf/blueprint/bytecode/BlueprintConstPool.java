package io.github.wordandahalf.blueprint.bytecode;

import javassist.bytecode.ConstPool;

import java.util.HashMap;

public class BlueprintConstPool {
    private ConstPool pool;
    private HashMap<String, HashMap<Integer, Integer>> updatedReferences;

    public BlueprintConstPool(String className) {
        this.pool = new ConstPool(className);
        this.updatedReferences = new HashMap<>();
    }

    /**
     * Adds the provided constant if it is not already in the ConstPool
     */
    public void addConstant(int index, ConstPool source) {
        int newIndex = source.copy(index, this.pool, null);

        if(updatedReferences.get(source.getClassName()) instanceof HashMap) {
            HashMap<Integer, Integer> map = updatedReferences.get(source.getClassName());

            map.put(index, newIndex);

            updatedReferences.put(source.getClassName(), map);
        } else {
            HashMap<Integer, Integer> map = new HashMap<>();

            map.put(index, newIndex);

            updatedReferences.put(source.getClassName(), map);
        }
    }

    /**
     * Adds any new constants from the provided ConstPool
     */
    public void addConstants(ConstPool source) {
        for(int i = 0; i < source.getSize(); i++) {
            int newIndex = source.copy(i, this.pool, null);

            if(updatedReferences.get(source.getClassName()) instanceof HashMap) {
                HashMap<Integer, Integer> map = updatedReferences.get(source.getClassName());

                map.put(i, newIndex);

                updatedReferences.put(source.getClassName(), map);
            } else {
                HashMap<Integer, Integer> map = new HashMap<>();

                map.put(i, newIndex);

                updatedReferences.put(source.getClassName(), map);
            }
        }
    }

    /**
     * Returns the updated indices of added constants
     * @return A HashMap organized by the added ConstPools' class names with its value being a HashMap of old indices and new indices
     */
    public HashMap<String, HashMap<Integer, Integer>> getUpdatedReferences() { return this.updatedReferences; }

    /**
     * @return An updated ConstPool
     */
    public ConstPool getConstPool() { return this.pool; }
}
