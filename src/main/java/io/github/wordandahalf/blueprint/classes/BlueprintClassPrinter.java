package io.github.wordandahalf.blueprint.classes;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.List;

import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.*;

public class BlueprintClassPrinter {
    public static String insnToString(AbstractInsnNode insn){
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString();
    }

    private static Printer printer = new Textifier();
    private static TraceMethodVisitor mp = new TraceMethodVisitor(printer);

    public static void print(ClassNode classNode, PrintWriter printWriter) {
        printWriter.println("Class " + classNode.name);

        final List<FieldNode> fields = classNode.fields;
        printWriter.println("Fields:");
        for(FieldNode field : fields) {
            printWriter.println("(" + field.desc + ") " + field.name);
        }

        final List<MethodNode> methods = classNode.methods;

        for(MethodNode m : methods) {

            final List<LocalVariableNode> localVariableNodes = m.localVariables;
            printWriter.println("Local variables:");
            for(LocalVariableNode variableNode : localVariableNodes) {
                printWriter.println("LocalVariable[" + variableNode.index + "] " + "(" + variableNode.desc + ") " + variableNode.name);
            }

            InsnList inList = m.instructions;
            printWriter.println(m.name);
            for(int i = 0; i< inList.size(); i++){
                printWriter.print("(" + inList.get(i).getClass().getSimpleName() + ")" + insnToString(inList.get(i)));
            }
        }
    }
}
