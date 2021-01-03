package org.gz.demo.config.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author gaozhao
 */
@Service
@Slf4j
public class SubscriberService extends MessageListenerAdapter {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String topic = redisTemplate.getStringSerializer().deserialize(message.getChannel()).toString();
        String body = redisTemplate.getStringSerializer().deserialize(message.getBody()).toString();

        log.info("Receive message: topic:{} body:{}", topic, body);
    }

}
