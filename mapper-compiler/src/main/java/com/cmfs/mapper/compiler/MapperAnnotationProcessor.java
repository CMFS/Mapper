package com.cmfs.mapper.compiler;

import com.cmfs.mapper.annotations.ClassMapper;
import com.cmfs.mapper.annotations.FieldMapper;
import com.cmfs.mapper.compiler.core.ClassContainer;
import com.cmfs.mapper.compiler.core.ClassReflection;
import com.cmfs.mapper.compiler.core.FieldContainer;
import com.cmfs.mapper.compiler.core.FieldReflection;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import static com.cmfs.mapper.compiler.Constants.CLASS_UTILS;
import static com.cmfs.mapper.compiler.Constants.DOT;
import static com.cmfs.mapper.compiler.Constants.FILE_COMMENT;
import static com.cmfs.mapper.compiler.Constants.FINDER;
import static com.cmfs.mapper.compiler.Constants.INTERFACE_MAPPER;
import static com.cmfs.mapper.compiler.Constants.INTERFACE_MAPPERS;
import static com.cmfs.mapper.compiler.Constants.INTERFACE_MAPPER_FACTORY;
import static com.cmfs.mapper.compiler.Constants.INTERFACE_REFLECTOR_METHOD;
import static com.cmfs.mapper.compiler.Constants.MAPPER_FINDER;
import static com.cmfs.mapper.compiler.Constants.METHOD_NAME_FIND;
import static com.cmfs.mapper.compiler.Constants.PACKAGE;
import static com.cmfs.mapper.compiler.Constants.PACKAGE_MAPPERS;
import static com.cmfs.mapper.compiler.Utils.sname;

/**
 * @author cmfs
 */

@AutoService(Processor.class)
public class MapperAnnotationProcessor extends BaseAnnotationProcessor {

    private Set<Class<? extends Annotation>> annotations;
    private ClassContainer classContainer;
    private FieldContainer fieldContainer;

    private Set<String> converterSet = new HashSet<>();

    public MapperAnnotationProcessor() {
        this.annotations = new HashSet<>();
        annotations.add(ClassMapper.class);
        annotations.add(FieldMapper.class);
    }

    @Override
    public Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        note("process ");

        classContainer = parseClassContainer(roundEnvironment);
        fieldContainer = parseFieldContainer(roundEnvironment, classContainer);


        if (!generateClassMappers(roundEnvironment)) {
            return false;
        }

        generateFindersJavaFile(roundEnvironment);
        return true;
    }

    private ClassContainer parseClassContainer(RoundEnvironment roundEnvironment) {
        ClassContainer classContainer = new ClassContainer();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(ClassMapper.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                continue;
            }
            TypeElement sourceTypeElement = (TypeElement) element;
            TypeElement targetTypeElement = getTargetTypeElement(sourceTypeElement.getAnnotation(ClassMapper.class));
            if (targetTypeElement == null) {
                continue;
            }

            // type#fieldName
            parseFields(classContainer, sourceTypeElement);
            parseFields(classContainer, targetTypeElement);

            ClassReflection classReflection = new ClassReflection();
            classReflection.setSourceTypeElement(sourceTypeElement);
            classReflection.setTargetTypeElement(targetTypeElement);
            classContainer.add(classReflection);
        }
        return classContainer;
    }

    private FieldContainer parseFieldContainer(RoundEnvironment roundEnvironment, ClassContainer classContainer) {
        FieldContainer fieldContainer = new FieldContainer();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(FieldMapper.class)) {
            if (element.getKind() != ElementKind.FIELD) {
                continue;
            }
            VariableElement variableElement = (VariableElement) element;
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();

            FieldMapper fieldMapper = variableElement.getAnnotation(FieldMapper.class);
            String targetFieldName = fieldMapper.value();

            String fieldName = variableElement.getSimpleName().toString();
            String className = typeElement.getQualifiedName().toString();

            String targetClassName = classContainer.getBySource(className).getTargetClassName();

            FieldReflection fieldReflection = new FieldReflection();
            fieldReflection.setClassName(className);
            fieldReflection.setFieldName(fieldName);
            fieldReflection.setTargetClassName(targetClassName);
            fieldReflection.setTargetFieldName(targetFieldName);
            fieldReflection.setTypeElement(typeElement);
            fieldReflection.setVariableElement(variableElement);
            note("read field: " + targetClassName + "#" + targetFieldName);
            fieldReflection.setTargetVariableElement(classContainer.get(targetClassName, targetFieldName));
            fieldContainer.add(fieldReflection);
        }
        return fieldContainer;
    }

    private void parseFields(ClassContainer container, TypeElement typeElement) {
        String className = typeElement.getQualifiedName().toString();
        String fieldName;
        for (Element subElement : typeElement.getEnclosedElements()) {
            if (subElement.getKind() == ElementKind.FIELD) {
                VariableElement variableElement = (VariableElement) subElement;
                fieldName = variableElement.getSimpleName().toString();
                container.put(className, fieldName, variableElement);
            }
        }
    }

    private boolean generateClassMappers(RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(ClassMapper.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                continue;
            }
            // 原类元素
            TypeElement sourceTypeElement = (TypeElement) element;

            ClassMapper classMapper = sourceTypeElement.getAnnotation(ClassMapper.class);
            // 目标类元素
            TypeElement targetTypeElement = getTargetTypeElement(classMapper);
            if (targetTypeElement == null) {
                return false;
            }
            note("find " + targetTypeElement);

            String paramName = Utils.decapitalize(Utils.getClassSimpleName(sourceTypeElement));
            String returnName = Utils.decapitalize(Utils.getClassSimpleName(targetTypeElement));
            ClassName returnClassName = Utils.getClassName(targetTypeElement);

            List<FieldReflection> fieldReflectionList = fieldContainer.get(sourceTypeElement.getQualifiedName().toString());
            CodeBlock.Builder attrsSetCodeBlockBuilder = CodeBlock.builder();
            if (!fieldReflectionList.isEmpty()) {
                attrsSetCodeBlockBuilder
                        .beginControlFlow("if ($L != null)", paramName);
                for (FieldReflection fieldReflection : fieldReflectionList) {
                    VariableElement src = fieldReflection.getVariableElement();
                    VariableElement dest = fieldReflection.getTargetVariableElement();
                    if (src.asType() != dest.asType()) {
                        attrsSetCodeBlockBuilder.add("// ");
                    }
                    attrsSetCodeBlockBuilder.add(fieldReflection.getSetMethodCode(returnName, paramName));
                }
                attrsSetCodeBlockBuilder.endControlFlow();
            }

            MethodSpec reflectMethod = MethodSpec.methodBuilder(INTERFACE_REFLECTOR_METHOD)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(Utils.getClassName(sourceTypeElement), paramName)
                    .returns(returnClassName)
                    .addCode(CodeBlock.builder()
                            .add("$T $L = new $T();\n", returnClassName, returnName, returnClassName)
                            .add(attrsSetCodeBlockBuilder.build())
                            .build())
                    .addCode("return $L;\n", returnName)
                    .build();

            String converterName = Utils.getReflectorSimpleName(sname(sourceTypeElement), sname(targetTypeElement));
            converterSet.add(PACKAGE_MAPPERS + DOT + converterName);
            TypeSpec typeSpec = TypeSpec.classBuilder(converterName)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(reflectMethod)
                    .addSuperinterface(Utils.getParameterizedTypeName(INTERFACE_MAPPER, sname(sourceTypeElement), sname(targetTypeElement)))
                    .build();
            generateJavaFile(JavaFile.builder(PACKAGE_MAPPERS, typeSpec));
        }
        return true;
    }

    private void generateFindersJavaFile(RoundEnvironment roundEnvironment) {
        if (!roundEnvironment.processingOver()) {
            return;
        }

        // Mappers  className/fieldName/Mapper

        TypeName src = Utils.getParameterizedTypeName(Class.class, "SRC");
        TypeName dest = Utils.getParameterizedTypeName(Class.class, "DEST");
        ClassName utilsClass = ClassName.bestGuess(CLASS_UTILS);

        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();

        ClassName mappersClassName = ClassName.bestGuess(INTERFACE_MAPPERS);
        ClassName mapperClassName = ClassName.bestGuess(INTERFACE_MAPPER);
        ClassName exceptionClassName = ClassName.get(Exception.class);
        ClassName runExceptionClassName = ClassName.get(RuntimeException.class);

        ParameterizedTypeName className = Utils.getParameterizedTypeName(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.bestGuess(INTERFACE_MAPPER)
        );
        FieldSpec fieldSpec = FieldSpec.builder(className, "map")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T<>(30)", ClassName.get(HashMap.class))
                .build();

        codeBlockBuilder.add("String mapperName = $T.getMapperName(srcClass, destClass);\n", utilsClass)
                .add("$T<SRC, DEST> mapper = map.get(mapperName);\n", mapperClassName)
                .beginControlFlow("synchronized($L.class)", MAPPER_FINDER)
                .beginControlFlow("if (mapper == null)")
                .add("try {\n")
                .indent()
                .add("mapper = $T.getMapperFactory().create(mapperName);\n", mappersClassName)
                .add("map.put(mapperName, mapper);\n")
                .unindent()
                .add("} catch($T e) {\n", exceptionClassName)
                .indent()
                .add("throw new $T(e);\n", runExceptionClassName)
                .unindent()
                .add("}\n")
                .endControlFlow()
                .add("mapper = map.get(mapperName);\n")
                .endControlFlow()
                .add("return mapper;\n");

        MethodSpec findMethodSpec = MethodSpec.methodBuilder(METHOD_NAME_FIND)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(src, "srcClass").build())
                .addParameter(ParameterSpec.builder(dest, "destClass").build())
                .addTypeVariable(TypeVariableName.get("SRC"))
                .addTypeVariable(TypeVariableName.get("DEST"))
                .returns(Utils.getParameterizedTypeName(INTERFACE_MAPPER, "SRC", "DEST"))
                .addCode(codeBlockBuilder.build())
                .build();

        TypeSpec typeSpec = TypeSpec
                .classBuilder(MAPPER_FINDER)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.bestGuess(FINDER))
                .addField(fieldSpec)
                .addMethod(findMethodSpec)
                .build();

        generateJavaFile(JavaFile.builder(PACKAGE, typeSpec));
    }


    private TypeElement getTargetTypeElement(ClassMapper classMapper) {
        TypeElement typeElement;
        try {
            classMapper.target();
            return null;
        } catch (MirroredTypeException mte) {
            TypeMirror typeMirror = mte.getTypeMirror();
            typeElement = (TypeElement) mTypeUtils.asElement(typeMirror);
        }
        String targetClassName = classMapper.value();
        boolean setTarget = ClassMapper.NONE.class.getCanonicalName().equals(typeElement.getQualifiedName().toString());
        if (!setTarget) {
            return typeElement;
        }
        // 没有指定类对象
        if ("".equals(targetClassName)) {
            // 没有配置类名称
            error("没有为%s指定target", typeElement.getQualifiedName());
            return null;
        }
        return mElementUtils.getTypeElement(targetClassName);
    }

    private void generateJavaFile(JavaFile.Builder javaFileBuilder) {
        generateJavaFile(javaFileBuilder
                .addFileComment(FILE_COMMENT)
                .build()
        );
    }

    private void generateJavaFile(JavaFile javaFile) {
        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            error(e);
        }
    }
}
