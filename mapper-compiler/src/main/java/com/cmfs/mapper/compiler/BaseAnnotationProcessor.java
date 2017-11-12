package com.cmfs.mapper.compiler;

import com.sun.source.util.Trees;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class BaseAnnotationProcessor extends AbstractProcessor {

    protected Filer mFiler;
    protected Messager mMessager;
    protected Elements mElementUtils;
    protected Types mTypeUtils;
    protected Trees mTrees;
    private Set<Class<? extends Annotation>> supportedAnnotations = new HashSet<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mMessager = processingEnvironment.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();
        mTypeUtils = processingEnvironment.getTypeUtils();
        mTrees = Trees.instance(processingEnvironment);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return supportedAnnotations;
    }

    @Override
    public final Set<String> getSupportedAnnotationTypes() {
        Set<Class<? extends Annotation>> supportedAnnotations = getSupportedAnnotations();
        Set<String> supportedAnnotationsTypes = new HashSet<>(supportedAnnotations.size());
        for (Class<? extends Annotation> annotationClass : supportedAnnotations) {
            supportedAnnotationsTypes.add(annotationClass.getCanonicalName());
        }
        return supportedAnnotationsTypes;
    }

    protected void error(Exception e) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, ":reflect-compiler: " + e.getLocalizedMessage());
    }

    protected void error(String s, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, ":reflect-compiler: " + String.format(s, args));
    }

    protected void note(String s, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, ":reflect-compiler: " + String.format(s, args));
    }

    protected void warn(String s, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.WARNING, ":reflect-compiler: " + String.format(s, args));
    }

}
