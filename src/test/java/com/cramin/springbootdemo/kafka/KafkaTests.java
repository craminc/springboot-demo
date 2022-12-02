package com.cramin.springbootdemo.kafka;

import com.cramin.springbootdemo.kafka.spring_kafka.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KafkaTests {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    void testSend() {
        kafkaProducer.produce("hello world!");
    }
}
