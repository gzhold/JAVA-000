package org.gz.mq;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(id = "consumer-1", topics = "test-topic-demo1")
    public void listen(String msg) {
        System.out.println("Receive : " + msg);
    }

}
