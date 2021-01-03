package org.gz.demo.config.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author gaozhao
 */
@Service
@Slf4j
public class PublishService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    boolean send(String topic, String data){
        redisTemplate.convertAndSend(topic, data);
        return true;
    }

}
