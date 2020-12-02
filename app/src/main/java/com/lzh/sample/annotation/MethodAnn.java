package com.lzh.sample.annotation;

import com.lzh.sample.entity.AnnEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodAnn {
    String methodName() default "noMethodName";

    AnnEnum annEnum() default AnnEnum.ONE;
}
