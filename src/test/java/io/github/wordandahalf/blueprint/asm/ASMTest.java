package io.github.wordandahalf.blueprint.asm;

import io.github.wordandahalf.blueprint.Foo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

public class ASMTest {
    private static Printer printer = new Textifier();
    private static TraceMethodVisitor mp = new TraceMethodVisitor(printer);

    public static String insnToString(AbstractInsnNode insn){
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString();
    }

    public static void main(String[] args) throws Exception {

        //read in, build classNode
        ClassNode classNode=new ClassNode();
        ClassReader cr=new ClassReader("io.github.wordandahalf.blueprint.Foo");
        cr.accept(classNode, 0);

        //peek at classNode and modifier
        List<MethodNode> methods=(List<MethodNode>)classNode.methods;

        MethodNode source = null;
        MethodNode target = null;

        for(MethodNode node : methods) {
            if(node.name.equals("sayBar_inject")) {
                source = node;
            }
            if(node.name.equals("sayBar")) {
                target = node;
            }
        }

        System.out.println("source code before: " + source.instructions.size());
        System.out.println("target code before: " + target.instructions.size());

        InsnList copyList = new InsnList();
        Iterator iterator = source.instructions.iterator();
        while(iterator.hasNext()) {
            AbstractInsnNode next = (AbstractInsnNode) iterator.next();

            if(next.getOpcode() != Opcodes.RETURN)
                copyList.add(next);
        }

        target.instructions.insert(copyList);

        System.out.println("source code after: " + source.instructions.size());
        System.out.println("target code after: " + target.instructions.size());

        iterator = target.instructions.iterator();
        while(iterator.hasNext()) {
            System.out.print(insnToString((AbstractInsnNode) iterator.next()));
        }

        /*
        for(MethodNode method: methods) {
            InsnList insnList=method.instructions;

            Iterator ite=insnList.iterator();
            while(ite.hasNext()) {
                AbstractInsnNode insn=(AbstractInsnNode)ite.next();
                int opcode=insn.getOpcode();

                if (opcode==RETURN) {
                    InsnList tempList = new InsnList();

                    tempList.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                    tempList.add(new LdcInsnNode("Returning ... "));
                    tempList.add(new MethodInsnNode(INVOKEVIRTUAL,"java/io/PrintStream","println", "(Ljava/lang/String;)V", false));
                    insnList.insert(insn.getPrevious(), tempList);
                }
            }
        }*/


        //write classNode

        ClassWriter out = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classNode.accept(out);

        Class<?> clazz = BlueprintClassLoader.defineClass("io.github.wordandahalf.blueprint.Foo", out.toByteArray());

        System.out.println("After load: ");

        Foo foo2 = new Foo();
        foo2.sayBar("Good evening");
    }

    /*
    public static void main(String[] args) throws Exception {
        classNodeTest();

        System.out.println("Done.");
    }

    private static void classPrinterTest() throws Exception {
        System.out.println("ClassPrinter:");
        ClassPrinter printer = new ClassPrinter();
        ClassReader printerReader = new ClassReader("io.github.wordandahalf.blueprint.Foo");
        printerReader.accept(printer, 0);
    }

    private static void classNodeTest() throws  Exception {
        System.out.println("ClassNode:");

        ClassNode node = new ClassNode();
        ClassReader nodeReader = new ClassReader("io.github.wordandahalf.blueprint.Foo");

        nodeReader.accept(node, 0);

        for(MethodNode methodNode : node.methods) {
            System.out.println("Modifying method " + methodNode.name + "(" + methodNode.desc + ")");

            InsnList methodInstructions = methodNode.instructions;
            Iterator instructionIterator = methodInstructions.iterator();

            while(instructionIterator.hasNext()) {
                AbstractInsnNode instruction = (AbstractInsnNode)instructionIterator.next();

                int opcode = instruction.getOpcode();

                if(opcode == Opcodes.RETURN) {
                    System.out.println("Added instructions before void return.");

                    InsnList newInstructions = new InsnList();

                    newInstructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                    newInstructions.add(new LdcInsnNode("Returning from " + methodNode.name));
                    newInstructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

                    methodNode.instructions.insert(instruction.getPrevious(), newInstructions);
                }
            }
        }

        ClassWriter nodeWriter = new ClassWriter(0);
        node.accept(nodeWriter);

        byte[] nodeBytecode = nodeWriter.toByteArray();

        BlueprintClassLoader nodeLoader = new BlueprintClassLoader();
        nodeLoader.defineClass("io.github.wordandahalf.blueprint.Foo", nodeBytecode);

        Foo nodeFoo = new Foo();

        System.out.println("foo#getFoo(): " + nodeFoo.getFoo());
        System.out.print("foo#sayBar(): ");
        nodeFoo.sayBar();
    }

    private static void classVisitorTest() throws  Exception {
        System.out.println("BlueprintClassVisitor:");
        ClassReader transformerReader = new ClassReader("io.github.wordandahalf.blueprint.Foo");
        ClassWriter transformerWriter = new ClassWriter(0);

        BlueprintClassVisitor transformer = new BlueprintClassVisitor(transformerWriter);

        transformerReader.accept(transformer, 0);

        byte[] bytecode = transformerWriter.toByteArray();

        BlueprintClassLoader transformerLoader = new BlueprintClassLoader();
        transformerLoader.defineClass("io.github.wordandahalf.blueprint.Foo", bytecode);

        Foo transformerFoo = new Foo();

        System.out.println("foo#getFoo(): " + transformerFoo.getFoo());
        System.out.print("foo#sayBar(): ");
        transformerFoo.sayBar();
    }*/
}
