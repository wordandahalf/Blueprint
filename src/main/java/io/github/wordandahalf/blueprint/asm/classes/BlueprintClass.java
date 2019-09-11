package io.github.wordandahalf.blueprint.asm.classes;

import io.github.wordandahalf.blueprint.asm.bytecode.BlueprintBytecodeModification;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

public class BlueprintClass extends ClassNode {
    public BlueprintClass() {
        super(Opcodes.ASM7);
    }

    public BlueprintClass(String className) throws IOException {
        this(className, ClassLoader.getSystemClassLoader());
    }

    public BlueprintClass(String className, ClassLoader loader) throws IOException {
        this();

        BlueprintClassReader reader = new BlueprintClassReader(className, loader);
        reader.accept(this, ClassReader.EXPAND_FRAMES);
    }

    @Override
    public String toString() {
        return "BlueprintClass(class_name='" + this.name + "',methods=" + this.methods.size() + ",fields=" + this.fields + ")";
    }

    public void apply(BlueprintBytecodeModification modification) {
        throw new UnsupportedOperationException("BlueprintClass#apply is unimplemented!");
    }
}
