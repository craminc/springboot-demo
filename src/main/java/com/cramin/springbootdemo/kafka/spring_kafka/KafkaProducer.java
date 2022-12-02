package com.cramin.springbootdemo.kafka.spring_kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void produce(String message) {
        kafkaTemplate.send("kafka-test", message);
    }
}
