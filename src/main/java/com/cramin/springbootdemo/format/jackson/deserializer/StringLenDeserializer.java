package com.cramin.springbootdemo.format.jackson.deserializer;

import com.cramin.springbootdemo.format.jackson.annotation.StringLen;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.util.Objects;

public class StringLenDeserializer extends JsonDeserializer<String> implements ContextualDeserializer {

    private int maxLen;

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        if (text == null || this.maxLen < 0 || this.maxLen >= text.length()) return text;
        return text.substring(0, this.maxLen);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        if (property != null) {
            if (Objects.equals(String.class, property.getType().getRawClass())) {
                StringLen annotation = property.getAnnotation(StringLen.class);
                if (annotation != null) this.maxLen = annotation.maxLen();
            }
        }
        return this;
    }
}
