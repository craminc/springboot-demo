package com.cramin.springbootdemo.kafka.origin.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class MsgProducerInterceptor implements ProducerInterceptor<String, String> {

    private volatile long suc = 0;
    private volatile long fal = 0;
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        return new ProducerRecord<>(
                record.topic(),
                record.partition(),
                record.timestamp(),
                record.key(),
                "cramin say: " + record.value(),
                record.headers()
        );
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception == null) suc++;
        else fal++;
    }

    @Override
    public void close() {
        double rat = (double) suc / (suc + fal);
        System.out.printf("[INFO] 发送成功率: %.2f%%\n" , rat * 100);
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
