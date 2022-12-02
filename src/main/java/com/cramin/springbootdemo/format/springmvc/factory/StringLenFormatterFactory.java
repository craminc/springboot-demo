package com.cramin.springbootdemo.format.springmvc.factory;

import com.cramin.springbootdemo.format.springmvc.StringLenFormatter;
import com.cramin.springbootdemo.format.springmvc.annotation.FieldLen;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.HashSet;
import java.util.Set;

public class StringLenFormatterFactory implements AnnotationFormatterFactory<FieldLen> {

    public final static Set<Class<?>> FIELD_TYPE = new HashSet<Class<?>>() {{
        add(String.class);
    }};

    @Override
    public Set<Class<?>> getFieldTypes() {
        return FIELD_TYPE;
    }

    @Override
    public Printer<?> getPrinter(FieldLen fieldLen, Class<?> fieldType) {
        return new StringLenFormatter();
    }

    @Override
    public Parser<?> getParser(FieldLen fieldLen, Class<?> fieldType) {
        StringLenFormatter formatter = new StringLenFormatter();
        formatter.setMax(fieldLen.max());
        return formatter;
    }
}
