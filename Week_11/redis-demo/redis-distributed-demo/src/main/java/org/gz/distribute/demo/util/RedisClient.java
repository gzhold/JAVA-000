package org.gz.distribute.demo.util;

/**
 * @author gaozhao
 */
public abstract class RedisClient {

    public abstract Boolean set(String key, String value, long expireTime);

}
