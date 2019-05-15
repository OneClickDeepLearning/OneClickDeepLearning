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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultKafkaConsumerService implements ConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultKafkaConsumerService.class);

    private Consumer<String, String> consumer;

    @Value("${kafka.group}")
    private String group;

    @Value("${kafka.topic}")
    private String topic;

    @Value("${bootstrap.servers.config}")
    private String bootstrapServersConfig;

    @Value("${auto.offset.reset.config}")
    private String autoOffsetResetConfig;

    @Value("${enable.auto.commit.config}")
    private String enableAutoCommitConfig;

    @Value("${auto.commit.interval.ms.config}")
    private String autoCommitIntervalMsConfig;

    @Value("${session.timeout.ms.config}")
    private String sessionTimeoutMsConfig;

    @Value("${key.deserializer.class.config}")
    private String keyDeserializerClassConfig;

    @Value("${value.deserializer.class.config}")
    private String valueDeserializerClassConfig;

    public DefaultKafkaConsumerService() { }

    @Override
    public void createConsumer() { if (consumer == null) createConsumerInstance();}

    private void createConsumerInstance() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetResetConfig); //earliest
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommitConfig); // 自动commit
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalMsConfig); // 自动commit的间隔
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMsConfig);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializerClassConfig);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializerClassConfig);
        consumer = new KafkaConsumer<String, String>(props);
    }

    @Override
    public void run(Client client) {

        createConsumer();

        logger.info("starting kafka consumer...");
        this.consumer.subscribe(Arrays.asList(topic));

        while (true) {
            logger.info("waiting message ....");
            ConsumerRecords<String, String> records = consumer.poll(Duration.of(1, ChronoUnit.DAYS));
            for (ConsumerRecord<String, String> record : records) {

                // message format: "fileName url"
                logger.info(String.format("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value()));

                if (record.value().trim().equals("")) {
                    continue;
                }
                client.downloadModel(record.value());
            }
        }
    }
}
