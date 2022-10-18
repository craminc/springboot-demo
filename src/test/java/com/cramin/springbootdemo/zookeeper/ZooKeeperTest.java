package com.cramin.springbootdemo.zookeeper;

import com.cramin.springbootdemo.zookeeper.lock.ZKLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.common.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
public class ZooKeeperTest {

    @Autowired
    private ZooKeeper zooKeeper;

    @Autowired
    private ZKLock lock;

    @Test
    void testConnect() {
        System.out.println(zooKeeper.getSessionId());
    }

    @Test
    void testCreate() throws InterruptedException, KeeperException {
        String path = zooKeeper.create("/lock/stock/seq-", "hello world".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(path);
    }

    @Test
    void testDelete() throws InterruptedException, KeeperException {
        zooKeeper.delete("/test", -1);
    }

    @Test
    void testNoLock() {
        CountDownLatch latch = new CountDownLatch(10);
        Runnable run = new Runnable() {
            private int stock = 5;
            @Override
            public void run() {
                try {
                    latch.await();
                    if (stock > 0) {
                        TimeUnit.SECONDS.sleep(1);
                        stock--;
                    }
                    System.out.println(stock);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(run).start();
            latch.countDown();
        }
    }

    @Test
    void testLock() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        Runnable run = new Runnable() {
            private int stock = 5;
            @Override
            public void run() {
                String path = "";
                try {
                    latch.await();
                    path = lock.lock("hello", 2, TimeUnit.SECONDS);
                    if (stock > 0) {
                        TimeUnit.SECONDS.sleep(1);
                        stock--;
                    }
                    log.info("stock: {}", stock);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (!StringUtils.isEmpty(path)) {
                        lock.unlock(path);
                    }
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(run).start();
            latch.countDown();
        }

        Thread.sleep(20000000);
    }

    public static void main(String[] args) {
        new ZooKeeperTest().testNoLock();
    }
}
