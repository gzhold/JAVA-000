package org.gz.distribute.demo.util;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTemplateClient extends RedisClient{

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Boolean set(String key, String value, long expireTime) {
        Boolean distributedLock = redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.MILLISECONDS);
        return distributedLock.booleanValue();
    }


    /**
     * 执行脚本
     * @param script
     * @param lockKey
     * @param key
     * @param result
     * @return
     */
    public boolean execute(String script, String lockKey, String key, long result) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long executeResult = (Long) redisTemplate.execute(redisScript, Collections.singletonList(lockKey), key);
        return Objects.equals(executeResult, result);
    }

}
