package com.cramin.springbootdemo.kafka.origin.interceptor;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;

public class MsgConsumerInterceptor implements ConsumerInterceptor<String, String> {
    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
        int size = 0;
        for (ConsumerRecord<String, String> ignored : records) size++;
        System.out.println("records size: " + size);
        return records;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
        offsets.forEach((tp, offset) -> {
            System.out.println(tp + "@" + offset);
        });
    }

    @Override
    public void close() {
        System.out.println(this.getClass().getName() + " close");
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
