package com.cramin.springbootdemo.redis.lock;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisLock {

    private final static String KEY_PREFIX = "emis:mgr:lock:";
    private String lockKey = "default_key";
    private String lockValue = "default_value";

    private StringRedisTemplate redisTemplate;

    public RedisLock() {}

    public RedisLock(String lockKey) {
        this.lockKey = lockKey;
    }

    public boolean lock(long expireTime, long waitTime, TimeUnit timeUnit) {
        this.lockValue = UUID.fastUUID().toString();
        return this.lock(this.lockValue, expireTime, waitTime, timeUnit);
    }

    public boolean lock(String value, long expireTime, long waitTime, TimeUnit timeUnit) {

        long wait = timeUnit.toMillis(waitTime);
        long start = System.currentTimeMillis();
        String key = KEY_PREFIX + this.lockKey;

        do {
            if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, timeUnit)))
                return true;
            if (wait < 100) break;
            // sleep for 100 millis
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                log.error("get lock error: {}", e.getMessage());
                break;
            }
        } while (System.currentTimeMillis() - start < wait);

        return false;
    }

    public void unlock() {
        String key = KEY_PREFIX + this.lockKey;
        String value = redisTemplate.opsForValue().get(key);
        if (this.lockValue.equals(value))
            redisTemplate.delete(key);
        else {
            log.info("not held this lock, skip");
        }
    }
}