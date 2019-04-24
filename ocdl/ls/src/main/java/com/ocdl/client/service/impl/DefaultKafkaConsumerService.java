package com.ocdl.client.service.impl;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Properties;

import com.ocdl.client.Client;
import com.ocdl.client.service.ConsumerService;
import org.apache.kafka.clients.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultKafkaConsumerService implements ConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultKafkaConsumerService.class);

    private Consumer<String, String> consumer;

    private String group = "js_group_anyway6";

    private String VSTOPIC = "mdmsg";

    public DefaultKafkaConsumerService() {
        createConsumerInstance();

    }

    @Override
    public void createConsumer() {
        if (consumer == null) createConsumerInstance();
    }

    private void createConsumerInstance() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "ec2-54-89-140-122.compute-1.amazonaws.com:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); //earliest
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true"); // 自动commit
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000"); // 自动commit的间隔
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);
    }

    @Override
    public void run(Client client) {

        logger.info("starting kafka consumer...");
        this.consumer.subscribe(Arrays.asList(VSTOPIC));

        while (true) {

            logger.info("waiting message ....");
            ConsumerRecords<String, String> records = consumer.poll(Duration.of(1, ChronoUnit.DAYS));
            for (ConsumerRecord<String, String> record : records) {

                // message format: "fileName url"
                logger.info("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value());

                client.downloadModel(record.value());
            }
        }
    }
}
