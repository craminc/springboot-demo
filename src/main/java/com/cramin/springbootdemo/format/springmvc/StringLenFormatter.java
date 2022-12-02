package com.cramin.springbootdemo.format.springmvc;

import lombok.Setter;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

@Setter
public class StringLenFormatter implements Formatter<String> {

    private int max;

    @Override
    public String parse(String text, Locale locale) throws ParseException {
        if (text.length() > max)
            text = text.substring(0, max);
        return text;
    }

    @Override
    public String print(String object, Locale locale) {
        return object;
    }
}
