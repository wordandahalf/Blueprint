package io.github.wordandahalf.blueprint.injection.bytecode;

import static javassist.bytecode.Opcode.*;

public class OpcodeUtils {
    public static int getNumberOfOperands(int opcode) {
        if(
            opcode == ALOAD ||
            opcode == ASTORE ||
            opcode == BIPUSH ||
            opcode == DLOAD ||
            opcode == FLOAD ||
            opcode == FSTORE ||
            opcode == ILOAD ||
            opcode == ISTORE ||
            opcode == LDC ||
            opcode == LLOAD ||
            opcode == LSTORE ||
            opcode == NEWARRAY ||
            opcode == RET
        ) return 1;

        if(
            opcode == ANEWARRAY ||
            opcode == CHECKCAST ||
            opcode == GETFIELD ||
            opcode == GETSTATIC ||
            opcode == GOTO ||
            opcode == IF_ACMPEQ ||
            opcode == IF_ACMPNE ||
            opcode == IF_ICMPEQ ||
            opcode == IF_ICMPGE ||
            opcode == IF_ICMPGT ||
            opcode == IF_ICMPLE ||
            opcode == IF_ICMPLT ||
            opcode == IF_ICMPNE ||
            opcode == IFEQ ||
            opcode == IFGE ||
            opcode == IFGT ||
            opcode == IFLE ||
            opcode == IFLT ||
            opcode == IFNE ||
            opcode == IFNONNULL ||
            opcode == IFNULL ||
            opcode == IINC ||
            opcode == INSTANCEOF ||
            opcode == INVOKESPECIAL ||
            opcode == INVOKESTATIC ||
            opcode == INVOKEVIRTUAL ||
            opcode == JSR ||
            opcode == LDC_W ||
            opcode == LDC2_W ||
            opcode == NEW ||
            opcode == PUTFIELD ||
            opcode == PUTSTATIC ||
            opcode == SIPUSH
        ) return 2;

        if(
            opcode == MULTIANEWARRAY
        ) return 3;

        if(
            opcode == GOTO_W ||
            opcode == INVOKEDYNAMIC ||
            opcode == INVOKEINTERFACE ||
            opcode == JSR_W
        ) return 4;

        if(
            opcode == WIDE
        ) return 5;

        if(
            opcode == TABLESWITCH
        ) return 16;

        return 0;
    }

    public static boolean referencesLocalVar(int opcode) {
        if(
            opcode == ALOAD ||
            opcode == ALOAD_0 ||
            opcode == ALOAD_1 ||
            opcode == ALOAD_2 ||
            opcode == ALOAD_3 ||
            opcode == ASTORE ||
            opcode == ASTORE_0 ||
            opcode == ASTORE_1 ||
            opcode == ASTORE_2 ||
            opcode == ASTORE_3 ||
            opcode == DLOAD ||
            opcode == DLOAD_0 ||
            opcode == DLOAD_1 ||
            opcode == DLOAD_2 ||
            opcode == DLOAD_3 ||
            opcode == DSTORE ||
            opcode == DSTORE_0 ||
            opcode == DSTORE_1 ||
            opcode == DSTORE_2 ||
            opcode == DSTORE_3 ||
            opcode == FLOAD ||
            opcode == FLOAD_0 ||
            opcode == FLOAD_1 ||
            opcode == FLOAD_2 ||
            opcode == FLOAD_3 ||
            opcode == FSTORE ||
            opcode == FSTORE_0 ||
            opcode == FSTORE_1 ||
            opcode == FSTORE_2 ||
            opcode == FSTORE_3 ||
            opcode == IINC ||
            opcode == ILOAD ||
            opcode == ILOAD_0 ||
            opcode == ILOAD_1 ||
            opcode == ILOAD_2 ||
            opcode == ILOAD_3 ||
            opcode == ISTORE ||
            opcode == ISTORE_0 ||
            opcode == ISTORE_1 ||
            opcode == ISTORE_2 ||
            opcode == ISTORE_3 ||
            opcode == LLOAD ||
            opcode == LLOAD_0 ||
            opcode == LLOAD_1 ||
            opcode == LLOAD_2 ||
            opcode == LLOAD_3 ||
            opcode == LSTORE ||
            opcode == LSTORE_0 ||
            opcode == LSTORE_1 ||
            opcode == LSTORE_2 ||
            opcode == LSTORE_3 ||
            opcode == RET
        ) return true;

        return false;
    }

    public static boolean isBranchInstruction(int opcode) {
        if(
            opcode == GOTO ||
            opcode == GOTO_W ||
            opcode == IF_ACMPEQ ||
            opcode == IF_ACMPNE ||
            opcode == IF_ICMPEQ ||
            opcode == IF_ICMPGE ||
            opcode == IF_ICMPGT ||
            opcode == IF_ICMPLE ||
            opcode == IF_ICMPLT ||
            opcode == IF_ICMPNE ||
            opcode == IFEQ ||
            opcode == IFGE ||
            opcode == IFGT ||
            opcode == IFLE ||
            opcode == IFLT ||
            opcode == IFNE ||
            opcode == IFNONNULL ||
            opcode == IFNULL ||
            opcode == JSR ||
            opcode == JSR_W
        ) return true;

        return false;
    }

    public static boolean referencesConstPool(int opcode) {
        if(
            opcode == ANEWARRAY ||
            opcode == CHECKCAST ||
            opcode == GETFIELD ||
            opcode == GETSTATIC ||
            opcode == INSTANCEOF ||
            opcode == INVOKEDYNAMIC ||
            opcode == INVOKEINTERFACE ||
            opcode == INVOKESPECIAL ||
            opcode == INVOKESTATIC ||
            opcode == INVOKEVIRTUAL ||
            opcode == MULTIANEWARRAY ||
            opcode == NEW ||
            opcode == PUTFIELD ||
            opcode == PUTSTATIC
        ) return true;

        return false;
    }
}
