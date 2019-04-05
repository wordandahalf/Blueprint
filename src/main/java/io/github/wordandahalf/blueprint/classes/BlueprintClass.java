package io.github.wordandahalf.blueprint.classes;

import io.github.wordandahalf.blueprint.transformers.ClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BlueprintClass {
    private ClassNode classNode;
    private ClassLoader classLoader;

    private String className;

    private Map<String, List<ClassTransformer>> transformerPool;

    private boolean defined = false;

    public BlueprintClass(ClassNode classNode) {
        this(classNode, ClassLoader.getSystemClassLoader());
    }

    public BlueprintClass(ClassNode classNode, ClassLoader loader) {
        this.classNode = classNode;
        this.className = classNode.name.replace("/", ".");

        this.transformerPool = new HashMap<>();
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
        this.transformerPool = new HashMap<>();
    }

    public ClassNode getClassNode() { return this.classNode; }
    public String getClassName() { return this.className; }

    public List<ClassTransformer> getTransformers() {
        LinkedList<ClassTransformer> list = new LinkedList<>();

        this.transformerPool.values().forEach(arrayList -> list.addAll(arrayList));

        return list;
    }

    public Set<String> getTransformersSources() { return this.transformerPool.keySet(); }
    public List<ClassTransformer> getTransformersBySource(String sourceClass) { return this.transformerPool.get(sourceClass); }

    public void addTransformers(String sourceClass, ClassTransformer... transformer) {
        if(this.defined)
            return; // TODO: Throw an exception

        List<ClassTransformer> list;

        if(this.transformerPool.containsKey(sourceClass)) {
            list = this.transformerPool.get(sourceClass);
        } else {
            list = new LinkedList<>();
        }

        list.addAll(Arrays.asList(transformer));

        this.transformerPool.put(sourceClass, list);
    }

    public Class defineClass() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        if(this.defined)
            return Class.forName(className); // TODO: Throw an exception

        BlueprintClassWriter writer = new BlueprintClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS, classLoader);
        this.classNode.accept(writer);

        Class clazz = ClassLoader.class;

        Method method = ((Class<?>) clazz).getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        method.setAccessible(true);

        byte[] bytecode = writer.toByteArray();

        Object result = method.invoke(this.classLoader, this.className, bytecode, 0, bytecode.length);

        Class reloadedClass = (Class) result;

        return reloadedClass;
    }
}
