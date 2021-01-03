package org.gz.distribute.demo.lock;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisCounterUtil {

    private final RedisTemplate<String, String> redisTemplate;

    private final String key;

    public RedisCounterUtil(RedisTemplate<String, String> redisTemplate, String key, int count) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(count));
    }

    public boolean decrease() {
        String countValue = this.redisTemplate.opsForValue().get(this.key);
        if (Integer.parseInt(countValue) == 0) {
            return false;
        }
        this.redisTemplate.opsForValue().decrement(this.key);
        return true;
    }

}
