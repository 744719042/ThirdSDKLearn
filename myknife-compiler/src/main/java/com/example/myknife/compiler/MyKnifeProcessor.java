package com.example.myknife.compiler;

import com.example.myknife.annotation.BindViewId;
import com.example.myknife.annotation.OnClickListener;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class MyKnifeProcessor extends AbstractProcessor {
    private Elements mElementUtils;
    private Filer mFiler;

    private Map<String, List<BindViewClass>> bindViewMap = new HashMap<>();
    private Map<String, List<OnClickListenerClass>> clickMap = new HashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(BindViewId.class.getName());
        set.add(OnClickListener.class.getName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.mElementUtils = processingEnvironment.getElementUtils();
        this.mFiler = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindViewId.class);
        for (Element element : elements) {
            VariableElement variableElement = (VariableElement) element;
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            BindViewClass bindViewClass = new BindViewClass();
            bindViewClass.field = variableElement.getSimpleName().toString();
            bindViewClass.id = variableElement.getAnnotation(BindViewId.class).value();
            bindViewClass.outerClass = typeElement.getQualifiedName().toString();
            bindViewClass.className = typeElement.getSimpleName().toString();
            bindViewClass.pkgName = mElementUtils.getPackageOf(typeElement).getQualifiedName().toString();
            List<BindViewClass> list = bindViewMap.get(bindViewClass.outerClass);
            if (list == null) {
                list = new ArrayList<>();
                bindViewMap.put(bindViewClass.outerClass, list);
            }
            list.add(bindViewClass);
        }

        Set<? extends Element> onclick = roundEnvironment.getElementsAnnotatedWith(OnClickListener.class);
        for (Element element : onclick) {
            ExecutableElement executableElement = (ExecutableElement) element;
            TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
            String className = typeElement.getQualifiedName().toString();
            List<OnClickListenerClass> list = clickMap.get(className);
            if (list == null) {
                list = new ArrayList<>();
                clickMap.put(className, list);
            }
            OnClickListenerClass onClick = new OnClickListenerClass();
            onClick.className = className;
            OnClickListener onClickListener = executableElement.getAnnotation(OnClickListener.class);
            onClick.ids = onClickListener.value();
            onClick.method = executableElement.getSimpleName().toString();
            list.add(onClick);
        }

        generateFile(bindViewMap);
        return false;
    }

    private void generateFile(Map<String, List<BindViewClass>> bindViewMap) {
        for (Map.Entry<String, List<BindViewClass>> entry : bindViewMap.entrySet()) {
            String className = entry.getKey();
            List<BindViewClass> list = entry.getValue();

            ClassName activityClassName = ClassName.bestGuess(className);
            FieldSpec.Builder fieldSpecBuilder = FieldSpec.builder(activityClassName, "mObject", Modifier.PRIVATE);

            MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("bind")
                    .returns(Void.TYPE)
                    .addParameter(ParameterSpec.builder(activityClassName, "object", Modifier.FINAL).build())
                    .addModifiers(Modifier.PUBLIC);
            methodSpecBuilder.addStatement("this.mObject = object");

            List<OnClickListenerClass> listenerClasses = clickMap.get(className);

            ClassName view = ClassName.get("android.view", "View");
            ClassName onClickListener = ClassName.get("android.view", "View.OnClickListener");
            for (BindViewClass bindViewClass : list) {
                methodSpecBuilder.addStatement("object.$L = object.findViewById($L)", bindViewClass.field, bindViewClass.id);
                for (OnClickListenerClass onClickListenerClass : listenerClasses) {
                    for (int id : onClickListenerClass.ids) {
                        if (id == bindViewClass.id) {
                            methodSpecBuilder.addCode("object.$L.setOnClickListener(new $T() { public void onClick($T view) { mObject.$L(view); } });", bindViewClass.field, onClickListener, view, onClickListenerClass.method);
                        }
                    }
                }
            }



            MethodSpec.Builder unbindBuilder = MethodSpec.methodBuilder("unbind").returns(Void.TYPE).addModifiers(Modifier.PUBLIC);
            for (BindViewClass bindViewClass : list) {
                unbindBuilder.addStatement("mObject.$L = null", bindViewClass.field);
            }

            ClassName unbindClassName = ClassName.get("com.example.myknife", "Unbinder");
            TypeSpec typeSpec = TypeSpec.classBuilder(list.get(0).className + "$$ViewBinding")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addSuperinterface(unbindClassName)
                    .addField(fieldSpecBuilder.build())
                    .addMethod(unbindBuilder.build())
                    .addMethod(methodSpecBuilder.build()).build();
            JavaFile javaFile = JavaFile.builder(list.get(0).pkgName, typeSpec).build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
