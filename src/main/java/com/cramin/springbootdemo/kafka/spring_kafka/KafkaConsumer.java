package com.cramin.springbootdemo.kafka.spring_kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(groupId = "group-1", topicPartitions = {
            @TopicPartition(topic = "kafka-test", partitionOffsets = {
                    @PartitionOffset(partition = "0", initialOffset = "-1")
            })
    })
    public void consume11(ConsumerRecord<String, String> consumerRecord, Consumer<String, String> consumer) {
        log.info("consumer-1-1: {}", consumerRecord.value());
    }

    @KafkaListener(topics = "kafka-test", groupId = "group-1")
    public void consume12(ConsumerRecord<String, String> consumerRecord, Consumer<String, String> consumer) {
        log.info("consumer-1-2: {}", consumerRecord.value());
        consumer.commitAsync();
    }

    @KafkaListener(topics = "kafka-test", groupId = "group-2")
    public void consume2(ConsumerRecord<String, String> consumerRecord, Consumer<String, String> consumer) {
        log.info("consumer-2: {}", consumerRecord.value());
    }
}
