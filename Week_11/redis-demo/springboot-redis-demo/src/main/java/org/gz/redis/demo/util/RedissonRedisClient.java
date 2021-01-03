package org.gz.redis.demo.util;


import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author gaozhao
 */
@Service
public class RedissonRedisClient implements RedisClient{

    @Autowired
    private RedissonClient redissonClient;


    /**
     * add key value
     *
     * @param key
     * @param value
     */
    @Override
    public void set(String key, String value) {
        redissonClient.getBucket(key).set(value);
    }

    /**
     * add key value, expire time
     *
     * @param key
     * @param value
     * @param liveTime ms
     */
    @Override
    public void set(byte[] key, byte[] value, long liveTime) {
        redissonClient.getBucket(new String(key, StandardCharsets.UTF_8)).set(value, liveTime, TimeUnit.MILLISECONDS);
    }

    /**
     * get key
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        RBucket<String> result = redissonClient.getBucket(key);
        return result.get();
    }

    /**
     * del key
     *
     * @param keys
     */
    @Override
    public long del(String... keys) {
        return redissonClient.getKeys().delete(keys);
    }

    /**
     * exists key
     *
     * @param key
     * @return
     */
    @Override
    public boolean exists(String key) {
        return redissonClient.getKeys().countExists(key) > 0;
    }
}
