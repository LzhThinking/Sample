package com.lzh.sample.entity;

import com.lzh.sample.annotation.ClassAnn;
import com.lzh.sample.annotation.MethodAnn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@ClassAnn(typeName = "EntityType")
public class Entity {

    public String name;

    public Entity() {
        getAnnotationName();
    }

    public String getAnnotationTypeName() {
        Class clazz = getClass();
        Annotation classAnn = clazz.getAnnotation(ClassAnn.class);
        if (classAnn instanceof ClassAnn) {
            return ((ClassAnn) classAnn).typeName();
        }
        return "no name";
    }

    public String getAnnotationName() {
        Class clazz = getClass();
        Method[] methods = clazz.getMethods();
        for (Method m: methods) {
            Annotation annotation = m.getAnnotation(MethodAnn.class);
            if (annotation != null) {
                name = ((MethodAnn) annotation).methodName();
                return name;
            }
        }
        return "no name";
    }

    @MethodAnn(methodName = "EntityGetName")
    public String getName() {
        return name;
    }
}
