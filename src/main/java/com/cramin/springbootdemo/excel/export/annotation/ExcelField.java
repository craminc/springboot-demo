package com.cramin.springbootdemo.excel.export.annotation;

import com.cramin.springbootdemo.excel.export.enums.SensitiveStrategy;
import org.apache.poi.ss.usermodel.CellType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

    /**
     * 字段标题
     */
    String title() default "";

    /**
     * 字段类型
     */
    CellType fieldType() default CellType.BLANK;

    /**
     * 字段顺序
     */
    int order() default 0;

    int cellWidth() default 20;

    SensitiveStrategy strategy() default SensitiveStrategy.NONE;

    String valMap() default "";
}
