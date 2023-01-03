package com.cramin.springbootdemo.excel.export.enums;

import cn.hutool.core.date.DateUtil;

import java.util.Date;
import java.util.function.Supplier;

public enum Suffix {
    NONE(() -> ""),
    UUID(() -> cn.hutool.core.lang.UUID.fastUUID().toString()),
    CURRENT_DATE(() -> DateUtil.formatDate(new Date())),
    CURRENT_TIME(() -> DateUtil.formatTime(new Date())),
    CURRENT_DATETIME(() -> DateUtil.formatDateTime(new Date()));

    private final Supplier<String> generator;

    Suffix(Supplier<String> generator) {
        this.generator = generator;
    }

    public Supplier<String> generator() {
        return generator;
    }
}
