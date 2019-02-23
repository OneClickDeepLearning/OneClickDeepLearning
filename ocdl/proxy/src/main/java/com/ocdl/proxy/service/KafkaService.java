package com.ocdl.proxy.service;

import com.ocdl.proxy.MessageTransferService;
import com.ocdl.proxy.Proxy;
import com.ocdl.proxy.domain.Topic;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class KafkaService implements MessageTransferService {

    private Consumer<String, String> consumer;
    private static KafkaProducer<String, String> producer;

    public static String KAFKADNS;
    private static String group = "js_group2";

    @Value("kafka.server.url")
    public static void setKAFKADNS(String KAFKADNS) {
        KafkaService.KAFKADNS = KAFKADNS;
    }

    public KafkaService() {
    }

    @Override
    public void createConsumer() {
        if (consumer == null) createConsumerInstance();
    }

    @Override
    public void createProducer() {
        if (producer == null) createProducerInstence();
    }

    private void createConsumerInstance() {
        // create kafka consumer
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKADNS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); //earliest
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true"); // 自动commit
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000"); // 自动commit的间隔
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);
    }


    private void createProducerInstence() {
        // create kafka producer
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKADNS);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        producer = new KafkaProducer<String, String>(props);
    }

    @Override
    public void send(Topic topic, String data) {

        try {
            producer.send(new ProducerRecord<String, String>(topic.toString(), data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void consum(Topic topic, Proxy proxy) {

        consumer.subscribe(Arrays.asList(topic.toString()));

        while (true) {

            System.out.println("consumer waiting jkmsg....");
            ConsumerRecords<String, String> records = consumer.poll(Duration.of(1, ChronoUnit.DAYS));

            for (ConsumerRecord<String, String> record : records) {
                // recieve the jkmsg, just a msg told that it has new model
                System.out.printf("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value());
                proxy.processMsg(record.value());
            }
        }
    }
}
