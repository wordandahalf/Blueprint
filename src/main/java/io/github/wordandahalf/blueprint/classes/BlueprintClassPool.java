package io.github.wordandahalf.blueprint.classes;

import io.github.wordandahalf.blueprint.logging.BlueprintLogger;
import org.objectweb.asm.tree.ClassNode;

import java.util.Collection;
import java.util.HashMap;

public class BlueprintClassPool {
    private HashMap<String, BlueprintClass> modifiedClasses;
    private HashMap<String, BlueprintClass> classes;

    public BlueprintClassPool() {
        this.modifiedClasses = new HashMap<>();
        this.classes = new HashMap<>();
    }

    public void addModifiedClass(BlueprintClass clazz) {
        BlueprintLogger.finest(BlueprintClassPool.class, "Adding modified ClassNode for '" + clazz.getClassName() + "'");

        modifiedClasses.put(clazz.getClassName(), clazz);
        classes.put(clazz.getClassName(), clazz);
    }

    public void addClass(BlueprintClass clazz) {
        BlueprintLogger.finest(BlueprintClassPool.class, "Adding ClassNode for '" + clazz.getClassName() + "'");

        classes.put(clazz.getClassName(), clazz);
    }

    public Collection<BlueprintClass> getModifiedClasses() { return modifiedClasses.values(); }

    public BlueprintClass getClass(String className) {
        if(modifiedClasses.get(className) != null) {
            BlueprintLogger.finest(BlueprintClassPool.class, "Loading a modified ClassNode for '" + className + "'");
            return modifiedClasses.get(className);
        }

        if(classes.get(className) != null) {
            BlueprintLogger.finest(BlueprintClassPool.class, "Loading a cached ClassNode for '" + className + "'");
            return classes.get(className);
        }

        return null;
    }
}
