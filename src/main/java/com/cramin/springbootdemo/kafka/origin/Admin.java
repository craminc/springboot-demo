package com.cramin.springbootdemo.kafka.origin;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DescribeTopicsResult;

import java.util.Collections;
import java.util.Properties;

public class Admin {

    private final static String BOOT_STRAP = "139.224.68.48:9092";
    private final static String TOPIC = "test-admin";

    private static Properties initCnf() {
        Properties props = new Properties();

        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOT_STRAP);
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);

        return props;
    }

    public static void main(String[] args) {
        Properties props = initCnf();
        try (AdminClient admin = AdminClient.create(props)) {
//            NewTopic topic = new NewTopic(TOPIC, 3, (short) 1);
//            System.out.println(topic.configs());
//            CreateTopicsResult topics = admin.createTopics(Collections.singleton(topic));
//            topics.all().get();
            DescribeTopicsResult res = admin.describeTopics(Collections.singleton(TOPIC));
            System.out.println(res.allTopicNames().get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
