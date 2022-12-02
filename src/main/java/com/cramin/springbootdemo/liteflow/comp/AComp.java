package com.cramin.springbootdemo.liteflow.comp;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

@LiteflowComponent("a")
@Slf4j
public class AComp extends NodeComponent {

    @Override
    public void process() throws Exception {
        log.info("A comp process...");
    }
}
