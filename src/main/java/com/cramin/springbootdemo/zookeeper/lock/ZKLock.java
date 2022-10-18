package com.cramin.springbootdemo.zookeeper.lock;

import com.cramin.springbootdemo.until.lock.DistributedLock;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ZKLock implements DistributedLock {

    private final static String KEY_PREFIX = "/lock/";
    private String lockKey = "stock";

    @Autowired
    private ZooKeeper zk;

    @Override
    public ZKLock getLock(@NotNull String lockKey) {
        ZKLock zkLock = new ZKLock();
        zkLock.lockKey = lockKey;
        return zkLock;
    }

    @Override
    public String lock(String data, int waitTime, TimeUnit timeUnit) {
        CountDownLatch latch = new CountDownLatch(1);
        String path = KEY_PREFIX + this.lockKey;
        try {
            String rPath = zk.create(path + "/seq-", data.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            List<String> children = zk.getChildren(path, false);
            if (ObjectUtils.isEmpty(children)) throw new RuntimeException("unexpected error");
            // sort by seq
            Collections.sort(children);
            // if current seq is the first child, get lock successful
            int idx = children.indexOf(rPath.substring(rPath.lastIndexOf("/") + 1));
            if (idx == 0) return rPath;
            String waitPath = path + "/" + children.get(idx - 1);
            Watcher watcher = event -> {
                if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
                    latch.countDown();
                }
            };
            zk.getData(waitPath, watcher, new Stat());
            // wait for lock
            if (latch.await(waitTime, timeUnit))
                return rPath;
            throw new RuntimeException("wait lock timeout");
        } catch (Exception e) {
            log.error("get lock error: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unlock(String path) {
        try {
            zk.delete(path, -1);
        } catch (Exception e) {
            log.error("unlock [{}] error: {}", path, e.getMessage());
        }
    }
}
