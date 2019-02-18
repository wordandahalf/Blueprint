package io.github.wordandahalf.blueprint;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.util.Collection;
import java.util.HashMap;

public class BlueprintClassPool {
    private ClassPool classPool;
    private HashMap<String, CtClass> modifiedClassPool;

    public BlueprintClassPool() {
        modifiedClassPool = new HashMap<>();
        classPool = ClassPool.getDefault();
    }

    protected void addModifiedClass(CtClass ctClass) {
        CtClass existingClass = modifiedClassPool.get(ctClass.getName());

        if(existingClass != null) {
            existingClass.detach();
        }

        modifiedClassPool.put(ctClass.getName(), ctClass);
    }

    protected CtClass getClass(String className) throws NotFoundException {
        if(modifiedClassPool.containsKey(className))
            return modifiedClassPool.get(className);

        return classPool.get(className);
    }

    protected Collection<CtClass> getModifiedClasses() { return this.modifiedClassPool.values(); }
}
