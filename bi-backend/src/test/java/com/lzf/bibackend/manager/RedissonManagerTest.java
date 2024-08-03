package com.lzf.bibackend.manager;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedissonManagerTest {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RedissonManager redissonManager;

    @Test
    public void testConnection() {
        try {
            redissonClient.getKeys().count();
            System.out.println("Successfully connected to Redis");
        } catch (Exception e) {
            System.out.println("Failed to connect to Redis: " + e.getMessage());
        }
    }

    @Test
    public void tryAcquire() throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            redissonManager.setupRateLimiter("1");
            boolean triedAcquire = redissonManager.tryAcquire("1");
            if(triedAcquire) System.out.println(i);
        }
        Thread.sleep(1000);
        for (int i = 0; i < 5; i++) {
            redissonManager.setupRateLimiter("1");
            boolean triedAcquire = redissonManager.tryAcquire("1");
            if(triedAcquire) System.out.println(i);
        }
    }
}