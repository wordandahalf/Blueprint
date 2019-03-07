package io.github.wordandahalf.blueprint.classes;

import io.github.wordandahalf.blueprint.logging.BlueprintLogger;

import java.util.Collection;
import java.util.HashMap;

public class BlueprintClassNodePool {
    private HashMap<String, BlueprintClass> modifiedClassNodes;
    private HashMap<String, BlueprintClass> classNodes;

    public BlueprintClassNodePool() {
        this.modifiedClassNodes = new HashMap<>();
        this.classNodes = new HashMap<>();
    }

    public void addModifiedClass(BlueprintClass blueprintClass) {
        BlueprintLogger.finest(BlueprintClassNodePool.class, "Adding modified ClassNode for '" + blueprintClass.getClassName() + "'");

        modifiedClassNodes.put(blueprintClass.getClassName(), blueprintClass);
        classNodes.put(blueprintClass.getClassName(), blueprintClass);
    }

    public void addClass(BlueprintClass blueprintClass) {
        BlueprintLogger.finest(BlueprintClassNodePool.class, "Adding ClassNode for '" + blueprintClass.getClassName() + "'");

        classNodes.put(blueprintClass.getClassName(), blueprintClass);
    }

    public Collection<BlueprintClass> getModifiedClasses() { return modifiedClassNodes.values(); }

    public BlueprintClass getClass(String className) {
        if(modifiedClassNodes.get(className) != null) {
            BlueprintLogger.finest(BlueprintClassNodePool.class, "Loading a modified ClassNode for '" + className + "'");
            return modifiedClassNodes.get(className);
        }

        if(classNodes.get(className) != null) {
            BlueprintLogger.finest(BlueprintClassNodePool.class, "Loading a cached ClassNode for '" + className + "'");
            return classNodes.get(className);
        }

        return null;
    }
}
