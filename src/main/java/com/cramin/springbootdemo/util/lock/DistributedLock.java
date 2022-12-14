package com.cramin.springbootdemo.util.lock;

import java.util.concurrent.TimeUnit;

public interface DistributedLock {

    DistributedLock getLock(String lockKey);

    String lock(String data, int waitTime, TimeUnit timeUnit);

    void unlock(String path);
}
