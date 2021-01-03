package org.gz.redis.demo.util;


import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author gaozhao
 */
@Service
public class RedisTemplateClient implements RedisClient{

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * add key value
     *
     * @param key
     * @param value
     */
    @Override
    public void set(String key, String value) {
        set(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8), 0L);
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
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key, value);
                if (liveTime > 0) {
                    connection.expire(key, liveTime);
                }
                return 1L;
            }
        });
    }

    /**
     * get key
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        return (String) redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return new String(redisConnection.get(key.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
            }
        });
    }

    /**
     * del key
     *
     * @param keys
     */
    @Override
    public long del(String... keys) {
        byte[][] keysBytes = new byte[keys.length][];
        int i = 0;
        for (String key : keys){
            keysBytes[i] = key.getBytes(StandardCharsets.UTF_8);
            i++;
        }
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.del(keysBytes);
            }
        });
    }

    /**
     * exists key
     *
     * @param key
     * @return
     */
    @Override
    public boolean exists(String key) {
        return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.exists(key.getBytes(StandardCharsets.UTF_8));
            }
        });
    }
}
