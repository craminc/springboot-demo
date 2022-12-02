package com.cramin.springbootdemo.liteflow.comp;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

@LiteflowComponent("c")
@Slf4j
public class CComp extends NodeComponent {

    @Override
    public void process() throws Exception {
        log.info("C comp process...");
    }
}
