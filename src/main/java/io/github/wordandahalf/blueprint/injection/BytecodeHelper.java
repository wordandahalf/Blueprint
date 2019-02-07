package io.github.wordandahalf.blueprint.injection;

import io.github.wordandahalf.blueprint.Blueprints;
import io.github.wordandahalf.blueprint.injection.bytecode.OpcodeUtils;
import io.github.wordandahalf.blueprint.utils.LoggingUtil;
import io.github.wordandahalf.blueprint.utils.Pair;

import javassist.bytecode.*;

import java.util.ArrayList;
import java.util.HashMap;

public class BytecodeHelper {
    /**
     * Copies (without duplicating) values in the source ConstPool into the target ConstPool
     * @param source The ConstPool to copy from
     * @param target The ConstPool to copy into
     * @return A {@link Pair} with the left value being the combined ConstPool and the right value a HashMap with the keys being the original indices and their respective values the new indices
     */
    public static Pair<ConstPool, HashMap<Integer, Integer>> combineConstPools(ConstPool source, ConstPool target) {
        HashMap<Integer, Integer> newIndices = new HashMap<>();

        for(int i = 0; i < source.getSize(); i++) {
            int newIndex = source.copy(i, target, null);

            newIndices.put(i, newIndex);
        }

        return Pair.of(target, newIndices);
    }

    /**
     * This method iterates through the provided code, updating any outdated ConstPool indices
     * @param code The provided bytecode
     * @param updatedIndices THe updated indices
     * @return The updated bytecode
     */
    public static byte[] updateConstPoolRefs(byte[] code, HashMap<Integer, Integer> updatedIndices) {
        for(int i = 0; i < code.length; i++) {
            if(OpcodeUtils.referencesConstPool(code[i] & 0xFF)
            && OpcodeUtils.getNumberOfOperands(code[i] & 0xFF) == 1) {
                int index = code[i + 1] & 0xFF;
                int newIndex = updatedIndices.get(index);

                code[i + 1] = (byte) (newIndex & 0x00FF);
            }
            else
            if(OpcodeUtils.referencesConstPool(code[i] & 0xFF)
                    && OpcodeUtils.getNumberOfOperands(code[i] & 0xFF) == 2) {
                int index = ((code[i + 1] & 0xFF) << 8) + (code[i + 2] & 0xFF);

                int newIndex;

                try {
                    newIndex = updatedIndices.get(index);
                } catch(NullPointerException e) { continue; }

                code[i + 1] = (byte) ((newIndex >> 8) & 0xFF00);
                code[i + 2] = (byte) (newIndex & 0x00FF);
            }
        }

        return code;
    }

    /**
     * Injects the source code into the target code at the specified index.
     * The returned bytecode should be without errors: this method automagically updates
     * the {@link ConstPool} and references to it in the code.
     * @param source The code to inject into the target code
     * @param target The target code to inject into
     * @param index The index in the bytecode to inject the target code into
     * @return
     */
    public static CodeAttribute inject(MethodInfo source, MethodInfo target, int index) {
        if(Blueprints.DEBUG_ENABLED)
            LoggingUtil.getLogger().fine("Injecting source bytecode at index " + index + " and target bytecode at index " + (source.getCodeAttribute().getCodeLength() - 1));

        Pair<ConstPool, HashMap<Integer, Integer>> updatedConstPool =
                combineConstPools(source.getConstPool(), target.getConstPool());

        Bytecode inject = new Bytecode(updatedConstPool.left,
                source.getCodeAttribute().getMaxStack() + target.getCodeAttribute().getMaxStack(),
                source.getCodeAttribute().getMaxLocals() + target.getCodeAttribute().getMaxLocals());

        byte[] sourceCode = source.getCodeAttribute().getCode();
        byte[] targetCode = target.getCodeAttribute().getCode();

        if(Blueprints.DEBUG_ENABLED) {
            LoggingUtil.getLogger().fine("Target bytecode:");
            dumpBytecode(targetCode);
        }

        byte[] injectCode = insertCode(
                updateConstPoolRefs(sourceCode, updatedConstPool.right),
                targetCode,
                index
        );

        if(Blueprints.DEBUG_ENABLED) {
            LoggingUtil.getLogger().fine("Final bytecode:");
            dumpBytecode(injectCode);
        }

        for(byte b : injectCode) {
            inject.add(b);
        }

        CodeAttribute codeAttribute = inject.toCodeAttribute();

        codeAttribute.setAttribute((StackMapTable) target.getCodeAttribute().getAttribute("StackMapTable"));

        return codeAttribute;
    }

    private static byte[] insertCode(byte[] source, byte[] target, int index) {
        ArrayList<Byte> code = new ArrayList<>();

        for(int i = 0; i < index; i++) {
            if((target[i] & 0xFF) == Opcode.RETURN)
                continue;

            if((target[i] & 0xFF) == Opcode.GOTO) {
                int soffset = (target[i + 1] << 8) | (target[i + 2]);
                int uoffset = (target[i+1]&0xFF << 8) + (target[i+2]&0xFF);

                LoggingUtil.getLogger().fine("Found goto instruction with offset: "
                        + soffset + "(" + uoffset + ")"
                );
            }

            code.add(target[i]);
        }

        for(byte b : source) {
            if((b & 0xFF) == Opcode.RETURN)
                continue;

            code.add(b);
        }

        for(int i = index; i < target.length; i++) {
            if((target[i] & 0xFF) == Opcode.GOTO) {
                int soffset = (target[i + 1] << 8) + (target[i + 2]);
                int uoffset = (target[i+1]&0xFF << 8) + (target[i+2]&0xFF);

                LoggingUtil.getLogger().fine("Found goto instruction with offset: "
                        + soffset + "(" + uoffset + ")"
                );
            }

            code.add(target[i]);
        }

        // Convert the ArrayList into a byte array -- ArrayLists cannot have primitives as types
        byte[] codeArray = new byte[code.size()];

        for (int i = 0; i < codeArray.length; i++) {
            codeArray[i] = code.get(i);
        }

        return codeArray;
    }

    private static void dumpBytecode(byte[] bytecode) {
        for(int i = 0; i < bytecode.length; i++) {
            try {
                String operands = "";

                int numberOfOperands = OpcodeUtils.getNumberOfOperands(bytecode[i] & 0xFF);

                if(numberOfOperands > 0) {
                    operands = "0x";

                    for (int j = 0; j < numberOfOperands; j++) {
                        operands += String.format("%02X", bytecode[i + j + 1]);
                    }
                }

                LoggingUtil.getLogger().fine("[" + String.format("0x%04X", i) + "] "
                        + Mnemonic.OPCODE[bytecode[i] & 0xFF]
                        + " " + operands + "("
                        + (OpcodeUtils.referencesConstPool(bytecode[i] & 0xFF) ? "P" : "")
                        + (OpcodeUtils.referencesLocalVar(bytecode[i] & 0xFF) ? "V" : "")
                        + (OpcodeUtils.isBranchInstruction(bytecode[i] & 0xFF) ? "B" : "") + ")");

                i += numberOfOperands;
            } catch(ArrayIndexOutOfBoundsException e) {
                LoggingUtil.getLogger().fine("[" + String.format("0x%04X", i) + "] "
                        + String.format("0x%02X", bytecode[i])
                        + "(" + bytecode[i] + ")");
            }
        }
    }
}
