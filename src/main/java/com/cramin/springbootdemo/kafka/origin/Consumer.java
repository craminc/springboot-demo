package com.cramin.springbootdemo.kafka.origin;

import com.cramin.springbootdemo.kafka.origin.interceptor.MsgConsumerInterceptor;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Consumer {

    private final static String BOOT_STRAP = "139.224.68.48:9092";
    private final static String TOPIC = "__consumer_offsets";
    private final static String GROUP_ID = "group.hello";
    private final static AtomicBoolean isRunning = new AtomicBoolean(true);

    private static Properties initCnf() {
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOT_STRAP);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, MsgConsumerInterceptor.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);

        return props;
    }

    public static void main(String[] args) {
        consumeSeek();
    }

    public static void consume() {
        Properties props = initCnf();
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singleton("topic-b"));
            Set<TopicPartition> assign = new HashSet<>();
            while (assign.size() == 0) {
                consumer.poll(Duration.ofMillis(100L));
                assign = consumer.assignment();
            }

            while (isRunning.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1L));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("%s-%d@%d: [%s]-[%d]\n",
                            record.topic(), record.partition(), record.offset(), record.value(), record.timestamp());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void consumeSeek() {
        Properties props = initCnf();
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            TopicPartition tp = new TopicPartition("topic-b", 0);
            consumer.assign(Collections.singleton(tp));
//            consumer.subscribe(Collections.singleton(TOPIC));
            Set<TopicPartition> assign = new HashSet<>();
            while (assign.size() == 0) {
                consumer.poll(Duration.ofMillis(100L));
                assign = consumer.assignment();
            }
            OffsetAndTimestamp offsetAndTimestamp = consumer.offsetsForTimes(Collections.singletonMap(tp, 1669629172108L)).get(tp);

            consumer.seek(tp, offsetAndTimestamp.offset());
            while (isRunning.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1L));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("%s-%d@%d: [%s]-[%d]\n",
                            record.topic(), record.partition(), record.offset(), record.value(), record.timestamp());
                }
                // kafka commitOffset can change as you wish
//                consumer.commitSync(Collections.singletonMap(tp, new OffsetAndMetadata(0)));
//                if (!records.isEmpty()) consumer.commitSync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void consumeOffset() {
//        Properties props = initCnf();
//        try (KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<>(props)) {
//            consumer.subscribe(Collections.singleton(TOPIC));
//            while (isRunning.get()) {
//                ConsumerRecords<byte[], byte[]> records = consumer.poll(Duration.ofSeconds(1L));
//                for (ConsumerRecord<byte[], byte[]> record : records) {
//                    BaseKey key = GroupMetadataManager.readMessageKey(ByteBuffer.wrap(record.key()));
//                    System.out.printf("[%s-%d@%d]-[%d]\n", record.topic(), record.partition(), record.offset(), record.timestamp());
//                    if (key instanceof OffsetKey) {
//                        if (record.value() != null) {
//                            System.out.println("offsetKey: " + key + "\n" +
//                                    "value: "+ GroupMetadataManager.readOffsetMessageValue(
//                                    ByteBuffer.wrap(record.value())
//                            )  + "\n");
//                        }
//                    } else if (key instanceof GroupMetadataKey) {
//                        if (record.value() != null) {
//                            System.out.println("groupMetadataKey: " + key + "\n" +
//                                    "value: " + GroupMetadataManager.readGroupMessageValue(
//                                    ((GroupMetadataKey) key).key(),
//                                    ByteBuffer.wrap(record.value()),
//                                    Time.SYSTEM
//                            )  + "\n");
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
