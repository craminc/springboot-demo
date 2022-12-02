package com.cramin.springbootdemo.liteflow;

import com.cramin.springbootdemo.liteflow.service.FlowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FlowTests {

    @Autowired
    private FlowService flowService;

    @Test
    void testFlow() {
        flowService.doService();
    }
}
