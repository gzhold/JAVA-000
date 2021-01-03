package org.gz.redis.demo.util;


import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gaozhao
 */
@Service
public class LettuceClient implements RedisClient{

    @Autowired
    private StatefulRedisConnection lettuce;


    /**
     * add key value
     *
     * @param key
     * @param value
     */
    @Override
    public void set(String key, String value) {
        lettuce.sync().set(key, value);
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
        lettuce.sync().set(key, value, SetArgs.Builder.px(liveTime));
    }

    /**
     * get key
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        return (String) lettuce.sync().get(key);
    }

    /**
     * del key
     *
     * @param keys
     */
    @Override
    public long del(String... keys) {
        return lettuce.sync().del(keys);
    }

    /**
     * exists key
     *
     * @param key
     * @return
     */
    @Override
    public boolean exists(String key) {
        return lettuce.sync().exists(key) > 0 ? true : false;
    }
}
