package org.gz.distribute.demo.controll;


import org.gz.distribute.demo.lock.RedisCounterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

@RestController
public class CounterController {

    private static final Logger logger = LoggerFactory.getLogger(CounterController.class);

    private static final int COUNT = 1000;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/test/counter")
    public String testCounter() {
        String uuid = UUID.randomUUID().toString();
        RedisCounterUtil redisCounter = new RedisCounterUtil(redisTemplate, uuid, 1000);
        for (int i = 0; i < COUNT; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        if (!redisCounter.decrease()) {
                            logger.info("Zero>>>");
                            break;
                        }
                        Thread.sleep(100 + new Random().nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return "Success";
    }

}
