package com.cramin.springbootdemo.liteflow.service;

import com.yomahub.liteflow.core.FlowExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FlowService {

    @Resource
    private FlowExecutor flowExecutor;

    public void doService() {
        flowExecutor.execute2Resp("chain1", "arg");
    }
}
