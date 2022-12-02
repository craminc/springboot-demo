package com.cramin.springbootdemo.kafka.origin;

import com.cramin.springbootdemo.kafka.origin.partitioner.DemoPartitioner;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {

    private final static String BOOTSTRAP = "139.224.68.48:9092";
    private final static String TOPIC = "topic-b";

    private static Properties initCnf() {
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DemoPartitioner.class);
//        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, MsgProducerInterceptor.class.getName());

        return props;
    }

    public static void main(String[] args) {
        Properties props = initCnf();
        try(KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            int i = 0;
            while (i++ < 100) {
                int finalI = i;
                ProducerRecord<String, String> record = new ProducerRecord<>(
                        TOPIC,
                        "msg" + finalI + ": hello Kafka!"
                );
                producer.send(record, (recordMetadata, e) -> {
                    if (e == null) {
                        System.out.println("msg " + finalI + " push to " + recordMetadata);
                    } else if (recordMetadata == null) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
