package org.gz.redis.demo.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

/**
 * @author gaozhao
 */
@Service
public class JedisClient implements RedisClient {

    @Autowired
    private Jedis jedis;

    /**
     * add key value
     *
     * @param key
     * @param value
     */
    @Override
    public void set(String key, String value) {
        jedis.set(key, value);
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
        jedis.set(key, value, SetParams.setParams().px(liveTime));
    }

    /**
     * get key
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        return jedis.get(key);
    }

    /**
     * del key
     *
     * @param keys
     */
    @Override
    public long del(String... keys) {
        return jedis.del(keys);
    }

    /**
     * exists key
     *
     * @param key
     * @return
     */
    @Override
    public boolean exists(String key) {
        return jedis.exists(key);
    }
}
