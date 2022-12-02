package com.cramin.springbootdemo.format.springmvc.annotation;

import java.lang.annotation.*;

/**
 * 格式化字段长度
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface FieldLen {
    int max();
}

