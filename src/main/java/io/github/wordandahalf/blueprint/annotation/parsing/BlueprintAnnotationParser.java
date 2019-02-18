package io.github.wordandahalf.blueprint.annotation.parsing;

import io.github.wordandahalf.blueprint.annotation.Blueprint;
import io.github.wordandahalf.blueprint.annotation.Inject;
import io.github.wordandahalf.blueprint.annotation.Overwrite;
import io.github.wordandahalf.blueprint.transformers.ClassTransformer;
import io.github.wordandahalf.blueprint.transformers.method.MethodInjectionInfo;
import io.github.wordandahalf.blueprint.transformers.method.MethodInjector;
import io.github.wordandahalf.blueprint.transformers.method.MethodOverwriter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BlueprintAnnotationParser {
    private Class<?> decoratedClass;
    private Blueprint blueprint;

    private String sourceClassName, targetClassName;

    private List<ClassTransformer> classTransformers;

    public BlueprintAnnotationParser(Class<?> decoratedClass, Blueprint blueprint) {
        this.decoratedClass = decoratedClass;
        this.blueprint = blueprint;

        this.sourceClassName = this.decoratedClass.getName();
        this.targetClassName = this.blueprint.target();

        this.classTransformers = new ArrayList<>();
    }

    public void parse() {
        for(Method m : this.decoratedClass.getDeclaredMethods()) {
            Overwrite overwrite = m.getAnnotation(Overwrite.class);
            Inject inject = m.getAnnotation(Inject.class);

            if(overwrite != null) {
                this.classTransformers.add(new MethodOverwriter(m.getName(), overwrite.target()));
            }

            if(inject != null) {
                this.classTransformers.add(new MethodInjector(
                        new MethodInjectionInfo(m.getName(), inject.target(), inject.at()))
                );
            }
        }
    }

    public String getSourceClassName() { return this.sourceClassName; }
    public String getTargetClassName() { return this.targetClassName; }

    public List<ClassTransformer> getClassTransformers() { return this.classTransformers; }
}
