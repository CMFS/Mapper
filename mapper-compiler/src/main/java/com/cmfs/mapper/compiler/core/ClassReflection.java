package com.cmfs.mapper.compiler.core;

import javax.lang.model.element.TypeElement;

/**
 * @author cmfs
 */

public class ClassReflection {

    private TypeElement sourceTypeElement;

    private TypeElement targetTypeElement;

    public TypeElement getSourceTypeElement() {
        return sourceTypeElement;
    }

    public void setSourceTypeElement(TypeElement sourceTypeElement) {
        this.sourceTypeElement = sourceTypeElement;
    }

    public TypeElement getTargetTypeElement() {
        return targetTypeElement;
    }

    public void setTargetTypeElement(TypeElement targetTypeElement) {
        this.targetTypeElement = targetTypeElement;
    }

    public String getSourceClassName() {
        return sourceTypeElement.getQualifiedName().toString();
    }

    public String getTargetClassName() {
        return targetTypeElement.getQualifiedName().toString();
    }
}
