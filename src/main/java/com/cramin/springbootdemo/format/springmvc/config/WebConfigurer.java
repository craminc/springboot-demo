package com.cramin.springbootdemo.format.springmvc.config;

import com.cramin.springbootdemo.format.springmvc.factory.StringLenFormatterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(new StringLenFormatterFactory());
    }
}
