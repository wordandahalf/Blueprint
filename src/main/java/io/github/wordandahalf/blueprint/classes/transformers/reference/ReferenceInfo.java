package io.github.wordandahalf.blueprint.classes.transformers.reference;

public class ReferenceInfo {
    public static enum ReferenceType {
        METHOD,
        FIELD
    }

    private ReferenceType sourceType, targetType;
    private String sourceClass, sourceName, targetClass, targetName;

    public ReferenceInfo(ReferenceType sourceType, String sourceClass, String sourceName, ReferenceType targetType, String targetClass, String targetName) {
        this.sourceType = sourceType;
        this.sourceClass = sourceClass.replace('.', '/');
        this.sourceName = sourceName;

        this.targetType = targetType;
        this.targetClass = targetClass.replace('.', '/');
        this.targetName = targetName;
    }

    public ReferenceType getSourceType() { return this.sourceType; }
    public String getSourceClass() { return this.sourceClass; }
    public String getSourceName() { return this.sourceName; }

    public ReferenceType getTargetType() { return this.targetType; }
    public String getTargetClass() { return this.targetClass; }
    public String getTargetName() { return this.targetName; }
}
