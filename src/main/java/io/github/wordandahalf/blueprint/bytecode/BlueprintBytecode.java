package io.github.wordandahalf.blueprint.bytecode;

import io.github.wordandahalf.blueprint.utils.IntegerUtils;
import javassist.bytecode.ConstPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlueprintBytecode {
    private BlueprintConstPool constPool;
    private List<Byte> code;

    /**
     * Constructs a BlueprintBytecode object
     * @param className The name of the class which this bytecode will be applied to
     */
    public BlueprintBytecode(String className) {
        this.constPool = new BlueprintConstPool(className);
        this.code = new ArrayList<>();
    }

    public Byte[] getBytecode() { return this.code.toArray(new Byte[] {}); }

    public void addConstant(int index, ConstPool pool) {
        this.constPool.addConstant(index, pool);
    }

    public void addConstants(ConstPool pool) {
        this.constPool.addConstants(pool);
    }

    /**
     * Appends bytecode to the end of the current bytecode
     * @param code
     */
    public void addBytecode(Byte... code) {
        this.addBytecode(0, code);
    }

    /**
     * Adds bytecode at the specified index without overwriting
     * @param index
     * @param code
     */
    public void addBytecode(int index, Byte... code) {
        //TODO
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

    private Byte[] updateConstPoolRef(Byte[] code) {
        int opcode = code[0] & 0xFF;

        int numberOfOperands = BlueprintOpcode.getNumberOfOperands(opcode);

        if(BlueprintOpcode.referencesConstPool(opcode)) {
            byte[] operands = new byte[numberOfOperands];

            for(int i = 1; i < numberOfOperands + 1; i++) {
                operands[i - 1] = code[i];
            }

            int oldIndex = IntegerUtils.fromBytes(operands);

            //TODO
        }

        return code;
    }
}
