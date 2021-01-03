package org.gz.demo.config.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PublishServiceTest {

    @Autowired
    private PublishService publishService;

    @Test
    void send() {
        publishService.send("sub-pub-demo", "send message!");
    }

}
