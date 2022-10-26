package com.cramin.springbootdemo.zookeeper.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

@Configuration
@Slf4j
public class ZKConfig {

    @Value("${zookeeper.connect-string}")
    private String connectString;

    @Value("${zookeeper.session-timeout}")
    private Integer sessionTimeout;

    @Value("${zookeeper.enable}")
    private boolean enable;

    private final CountDownLatch lock = new CountDownLatch(1);

    @Bean
    public ZooKeeper zooKeeper() {
        if (!enable) return null;
        ZooKeeper zk;
        try {
            zk = new ZooKeeper(connectString, sessionTimeout, event -> {
                log.info("receive zk watcher notice: {}", event);
                if (Watcher.Event.KeeperState.SyncConnected == event.getState())
                    lock.countDown();
            });
            // wait for connected
            lock.await();
            log.info("init zookeeper connection state: {}", zk.getState());
        } catch (Exception e) {
            log.error("init zookeeper connection error: {}", e.getMessage());
            throw new RuntimeException("init zookeeper connection error");
        }

        return zk;
    }
}
