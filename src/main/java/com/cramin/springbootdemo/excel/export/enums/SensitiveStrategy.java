package com.cramin.springbootdemo.excel.export.enums;

import java.util.function.Function;

public enum SensitiveStrategy {
    /**
     * 不处理
     */
    NONE(s -> s),

    /**
     * 用户名
     */
    USERNAME(s -> s.replaceAll("(\\S{2})\\S(\\S*)", "$1*$2")),
    /**
     * 身份证
     */
    ID_CARD(s -> s.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1****$2")),
    /**
     * 手机号
     */
    PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));

    private final Function<String, String> desensitizer;

    SensitiveStrategy(Function<String, String> desensitizer) {
        this.desensitizer = desensitizer;
    }

    public Function<String, String> desensitizer() {
        return desensitizer;
    }

}
