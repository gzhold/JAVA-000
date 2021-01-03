package org.gz.redis.demo.util;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LettuceClientTest {
    @Autowired
    private LettuceClient lettuceClient;

    @Test
    void set() {
        String key = "test01";
        lettuceClient.set(key, key);
        assertEquals(key, lettuceClient.get(key));
    }

}
