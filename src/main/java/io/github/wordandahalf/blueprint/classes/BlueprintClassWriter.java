package io.github.wordandahalf.blueprint.classes;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class BlueprintClassWriter extends ClassWriter {
    private ClassLoader loader;

    public BlueprintClassWriter(ClassReader reader, int flags, ClassLoader loader) {
        super(reader, flags);

        this.loader = loader;
    }

    public BlueprintClassWriter(int flags, ClassLoader loader) {
        this(null, flags, loader);
    }

    @Override
    protected ClassLoader getClassLoader() {
        return this.loader;
    }
}
