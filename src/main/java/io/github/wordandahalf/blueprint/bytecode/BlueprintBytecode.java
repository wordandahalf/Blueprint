package io.github.wordandahalf.blueprint.bytecode;

import javassist.bytecode.ConstPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlueprintBytecode {
    private ConstPool constPool;
    private List<Byte> code;

    /**
     * Constructs a BlueprintBytecode object
     * @param className The name of the class which this bytecode will be applied to
     */
    public BlueprintBytecode(String className) {
        this.constPool = new ConstPool(className);
        this.code = new ArrayList<>();
    }

    public Byte[] getBytecode() { return this.code.toArray(new Byte[] {}); }

    /**
     * Appends bytecode to the end of the current bytecode
     * @param code
     */
    public void addBytecode(Byte... code) {
        this.code.addAll(Arrays.asList(code));
    }

    /**
     * Adds bytecode at the specified index without overwriting
     * @param index
     * @param code
     */
    public void addBytecode(int index, Byte... code) {
        this.code.addAll(index, Arrays.asList(code));
    }

    /**
     * Replaces all of the current bytecode with the provided array
     * @param code
     */
    public void setBytecode(Byte... code) {
        this.code = Arrays.asList(code);
    }

    /**
     * Replaces the bytecode starting at the index with the provided bytecode.
     * If the provided bytecode exceeds the length of the existing bytecode, the offending bytecode
     * is appended to the end.
     * @param index
     * @param code
     */
    public void setBytecode(int index, Byte... code) {
        int i = index;

        for(Byte b : code) {
            if(i > (this.code.size() - 1)) {
                this.code.add(b);
            } else {
                this.code.set(i, b);
            }

            i++;
        }
    }
}
