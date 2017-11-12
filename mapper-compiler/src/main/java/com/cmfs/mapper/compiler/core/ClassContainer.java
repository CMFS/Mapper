package com.cmfs.mapper.compiler.core;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @author cmfs
 */

public class ClassContainer {

    private final Map<String, ClassReflection> sourceMap = new HashMap<>();
    private final Map<String, ClassReflection> targetMap = new HashMap<>();

    // type#field
    private final Map<String, VariableElement> varMap = new HashMap<>();

    private static String getName(TypeElement typeElement) {
        return typeElement.getQualifiedName().toString();
    }

    public void add(ClassReflection classReflection) {
        sourceMap.put(getName(classReflection.getSourceTypeElement()), classReflection);
        targetMap.put(getName(classReflection.getTargetTypeElement()), classReflection);
    }

    public ClassReflection getBySource(String className) {
        return sourceMap.get(className);
    }

    public ClassReflection getByTarget(String className) {
        return targetMap.get(className);
    }

    public TypeElement getElementBySource(String className) {
        return sourceMap.get(className).getSourceTypeElement();
    }

    public TypeElement getElementByTarget(String className) {
        return targetMap.get(className).getTargetTypeElement();
    }

    public void put(String className, String fieldName, VariableElement fieldElement) {
        varMap.put(className + "#" + fieldName, fieldElement);
    }

    public VariableElement get(String className, String fieldName) {
        return varMap.get(className + "#" + fieldName);
    }

}
