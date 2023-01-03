package com.cramin.springbootdemo.excel.export.annotation;

import com.cramin.springbootdemo.excel.export.enums.Suffix;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelModel {

    String title() default "";

    Suffix titleSuffix() default Suffix.NONE;

    String[] sheetNames() default {};
}
