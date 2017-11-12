package com.cmfs.mapper.compiler.core;

import com.cmfs.mapper.compiler.Utils;
import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @author cmfs
 */

public class FieldReflection {

    private String className;

    private String fieldName;

    private String targetClassName;

    private String targetFieldName;

    private TypeElement typeElement;

    private VariableElement variableElement;
    private VariableElement targetVariableElement;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    public void setTargetClassName(String targetClassName) {
        this.targetClassName = targetClassName;
    }

    public String getTargetFieldName() {
        return targetFieldName;
    }

    public void setTargetFieldName(String targetFieldName) {
        this.targetFieldName = targetFieldName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public VariableElement getVariableElement() {
        return variableElement;
    }

    public void setVariableElement(VariableElement variableElement) {
        this.variableElement = variableElement;
    }

    public VariableElement getTargetVariableElement() {
        return targetVariableElement;
    }

    public void setTargetVariableElement(VariableElement targetVariableElement) {
        this.targetVariableElement = targetVariableElement;
    }

    public String getFieldPath() {
        return className + "#" + fieldName;
    }

    public String getTargetFieldPath() {
        return targetClassName + "#" + targetFieldName;
    }

    // 仅限于同类型
    public CodeBlock getSetMethodCode(String setVar, String getVar) {
        return CodeBlock.of("$L.set$L($L.get$L());\n", setVar, Utils.capitalize(getTargetFieldName()), getVar, Utils.capitalize(getFieldName()));
    }

}
