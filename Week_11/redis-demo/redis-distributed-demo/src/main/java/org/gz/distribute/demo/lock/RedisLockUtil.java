package org.gz.distribute.demo.lock;

import org.gz.distribute.demo.util.RedisClient;
import org.gz.distribute.demo.util.RedisTemplateClient;


public class RedisLockUtil {

    private static final String LOCK_KEY = "distributed_lock";

    private static final String RELEASE_LOCK_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";;

    private static final long RELEASE_LOCK_SUCCESS_RESULT = 1L;

    private RedisLockUtil() {}


    /**
     * 获取锁
     * @param redisClient
     * @param lockKey
     * @param expireTime ms
     * @return
     */
    public static boolean tryGetDistributedLock(RedisClient redisClient, String lockKey, long expireTime) {
        if (null == redisClient) {
            return false;
        }
        try {
            return redisClient.set(LOCK_KEY, lockKey, expireTime);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 释放锁
     * @param redisClient
     * @param key
     * @return
     */
    public static boolean releaseDistributedLock(RedisClient redisClient, String key) {
        if (redisClient instanceof RedisTemplateClient) {
            RedisTemplateClient redisTemplateClient = (RedisTemplateClient) redisClient;
            return redisTemplateClient.execute(RELEASE_LOCK_LUA_SCRIPT, LOCK_KEY, key, RELEASE_LOCK_SUCCESS_RESULT);
        }
        return false;
    }

}
