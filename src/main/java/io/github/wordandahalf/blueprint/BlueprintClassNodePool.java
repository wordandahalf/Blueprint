package io.github.wordandahalf.blueprint;

import io.github.wordandahalf.blueprint.logging.BlueprintLogger;
import org.objectweb.asm.tree.ClassNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;

public class BlueprintClassNodePool {
    private HashMap<String, ClassNode> modifiedClassNodes;
    private HashMap<String, ClassNode> classNodes;

    public BlueprintClassNodePool() {
        this.modifiedClassNodes = new HashMap<>();
        this.classNodes = new HashMap<>();
    }

    public void addModifiedClassNode(ClassNode node) {
        // TODO: Use logging utility
        BlueprintLogger.log(Level.FINEST, BlueprintClassNodePool.class, "Adding modified ClassNode for '" + node.name + "'");

        modifiedClassNodes.put(node.name.replace("/", "."), node);
        classNodes.put(node.name.replace("/", "."), node);
    }

    public void addClassNode(ClassNode node) {
        // TODO: Use logging utility
        BlueprintLogger.log(Level.FINEST, BlueprintClassNodePool.class, "Adding ClassNode for '" + node.name + "'");

        classNodes.put(node.name.replace("/", "."), node);
    }

    public Collection<ClassNode> getModifiedClassNodes() { return modifiedClassNodes.values(); }

    public ClassNode getClassNode(String className) {
        if(modifiedClassNodes.get(className) != null) {
            // TODO: Use logging utility
            BlueprintLogger.log(Level.FINEST, BlueprintClassNodePool.class, "Loading a modified ClassNode for '" + className + "'");
            return modifiedClassNodes.get(className);
        }

        if(classNodes.get(className) != null) {
            BlueprintLogger.log(Level.FINEST, BlueprintClassNodePool.class, "Loading a cached ClassNode for '" + className + "'");
            return classNodes.get(className);
        }

        return null;
    }
}
