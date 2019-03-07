package io.github.wordandahalf.blueprint.classes;

import io.github.wordandahalf.blueprint.loading.BlueprintClassReader;
import io.github.wordandahalf.blueprint.loading.BlueprintClassWriter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BlueprintClass {
    private ClassNode classNode;
    private ClassLoader classLoader;

    private String className;

    public BlueprintClass(ClassNode classNode) {
        this(classNode, ClassLoader.getSystemClassLoader());
    }

    public BlueprintClass(ClassNode classNode, ClassLoader loader) {
        this.classNode = classNode;
        this.className = classNode.name.replace("/", ".");
    }

    public BlueprintClass(String className) throws IOException {
        this(className, ClassLoader.getSystemClassLoader());
    }

    public BlueprintClass(String className, ClassLoader loader) throws IOException  {
        this.className = className;
        this.classLoader = loader;

        ClassNode node = new ClassNode();
        BlueprintClassReader reader = new BlueprintClassReader(className, loader);
        reader.accept(node, ClassReader.EXPAND_FRAMES);

        this.classNode = node;
    }

    public ClassNode getClassNode() { return this.classNode; }

    public String getClassName() { return this.className; }

    public Class defineClass() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        BlueprintClassWriter writer = new BlueprintClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS, classLoader);
        this.classNode.accept(writer);

        Class clazz = ClassLoader.class;

        Method method = clazz.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        method.setAccessible(true);

        byte[] bytecode = writer.toByteArray();

        Object result = method.invoke(this.classLoader, this.className, bytecode, 0, bytecode.length);

        Class reloadedClass = (Class) result;

        return reloadedClass;
    }
}
