package org.gz.redis.demo;


import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value(("${spring.redis.port}"))
    private int port;


    @Bean(name = "jedis")
    public Jedis jedis(){
        return new Jedis(host, port);
    }

    @Bean(name = "lettuce")
    public StatefulRedisConnection lettuce(){
        RedisClient client = RedisClient.create(new RedisURI(host, port, Duration.ofSeconds(10)));
        StatefulRedisConnection<String, String> connection = client.connect();
        return connection;
    }

    @Bean(name = "redissonClient")
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                    .setAddress("redis://127.0.0.1:6379")
                    .setDatabase(1);
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
