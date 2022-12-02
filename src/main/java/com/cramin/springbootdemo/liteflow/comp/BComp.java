package com.cramin.springbootdemo.liteflow.comp;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

@LiteflowComponent("b")
@Slf4j
public class BComp extends NodeComponent {

    @Override
    public void process() throws Exception {
        log.info("B comp process...");
    }
}
