package org.gz.redis.demo.util;

/**
 * @author gaozhao
 */
public interface RedisClient {

    /**
     * add key value
     *
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * add key value, expire time
     *
     * @param key
     * @param value
     * @param liveTime ms
     */
    void set(byte[] key, byte[] value, long liveTime);

    /**
     * get key
     * @param key
     * @return
     */
    String get(String key);


    /**
     * del key
     *
     * @param keys
     */
    long del(String... keys);

    /**
     * exists key
     *
     * @param key
     * @return
     */
    boolean exists(String key);

}
