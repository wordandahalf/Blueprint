package io.github.wordandahalf.blueprint.environment;

import io.github.wordandahalf.blueprint.asm.classes.BlueprintClass;
import vlsi.utils.CompactHashMap;

import java.io.IOException;

/**
 * Manages the loading and unloading of ASM7 {@link BlueprintClass} into memory
 */
public class BlueprintClassManager {
    private  static CompactHashMap<String, BlueprintClass> classes = new CompactHashMap<>();

    /**
     * Loads into memory a class with the provided fully-qualified Java class name from disk
     * @return The loaded class (if any, otherwise null). If the class is already loaded, then it will return the already-loaded class
     */
    public static BlueprintClass get(String className) {
        if(classes.get(className) == null) {
            try {
                BlueprintClass newClass = new BlueprintClass(className);

                classes.put(className, newClass);

                return newClass;
            } catch(IOException e) {
                // TODO: Logging
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        } else {
            return classes.get(className);
        }

        return null;
    }

    /**
     * Adds an already-loaded class into the system
     * @param overwrite If true, the provided class will overwrite any class already loaded with the same class name
     */
    public static void add(BlueprintClass blueprintClass, boolean overwrite) {
        throw new UnsupportedOperationException("BlueprintClassManager#add is unimplemented!");
    }

    /**
     * Writes the loaded class with changes to disk
     * @return A reference to the newly modified class
     */
    public static Class<?> write(String className) {
        return null;
    }

    /**
     * Removes the class from memory without writing anything to disk
     */
    public static void remove(String className) {
        throw new UnsupportedOperationException("BlueprintClassManager#unload is unimplemented!");
    }
}
