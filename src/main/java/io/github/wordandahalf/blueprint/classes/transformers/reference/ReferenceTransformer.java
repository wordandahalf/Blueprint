package io.github.wordandahalf.blueprint.classes.transformers.reference;

import io.github.wordandahalf.blueprint.classes.BlueprintClass;
import io.github.wordandahalf.blueprint.classes.transformers.ClassTransformer;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class ReferenceTransformer extends ClassTransformer {
    protected final ReferenceInfo info;

    public ReferenceTransformer(ReferenceInfo info) {
        this.info = info;
    }

    public ReferenceInfo getReferenceInfo() { return this.info; }

    public BlueprintClass apply(final BlueprintClass sourceClass, final BlueprintClass targetClass) throws Exception {
        // To prevent ConcurrentModificationException, a traditional for-each loop cannot be used
        ArrayList<MethodNode> methodsToAdd = new ArrayList<>();

        for(Iterator<MethodNode> iterator = targetClass.getClassNode().methods.iterator(); iterator.hasNext();) {
            MethodNode method = iterator.next();

            MethodNode modifiedMethod = this.apply(method, this.info);

            iterator.remove();
            methodsToAdd.add(modifiedMethod);
        }

        targetClass.getClassNode().methods.addAll(methodsToAdd);

        return targetClass;
    }

    public abstract MethodNode apply(final MethodNode targetMethod, final ReferenceInfo info);
}
