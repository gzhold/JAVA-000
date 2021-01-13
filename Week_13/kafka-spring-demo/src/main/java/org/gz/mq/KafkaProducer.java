package org.gz.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "test-topic-demo1";

    private final int count = 15;

    public void produce() {
        for (int i = 0; i < count; i++) {
            kafkaTemplate.send(TOPIC, "test message " + i);
        }
    }

}
