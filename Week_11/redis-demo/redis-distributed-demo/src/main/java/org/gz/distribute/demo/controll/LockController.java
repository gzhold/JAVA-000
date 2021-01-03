package org.gz.distribute.demo.controll;


import org.gz.distribute.demo.lock.RedisLockUtil;
import org.gz.distribute.demo.util.RedisTemplateClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author gaozhao
 */
@RestController
public class LockController {

    private static final Logger logger = LoggerFactory.getLogger(LockController.class);

    private static final long TIME_OUT = 5000;

    @Autowired
    private RedisTemplateClient redisClient;

    @GetMapping("/test/lock")
    public String distributedLockTest() throws InterruptedException {
        String uuid = UUID.randomUUID().toString();
        logger.info("Attempt to acquire a distributed lock: {}", uuid);
        if (RedisLockUtil.tryGetDistributedLock(redisClient, uuid, TIME_OUT)) {
            logger.info("Successfully obtained distributed lock :{}", uuid);
            // 模拟业务耗时
            Thread.sleep(3000);
            if (RedisLockUtil.releaseDistributedLock(redisClient, uuid)) {
                return uuid;
            }
            return "Unlock failed :" + uuid;
        }
        logger.info("Failed to acquire distributed lock :{}", uuid);
        return "Get lock failed!";
    }

}
