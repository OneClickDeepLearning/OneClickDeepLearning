package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.service.MessageQueueService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Properties;

@Component
public class KafkaService implements MessageQueueService {

    private static final Logger log = Logger.getLogger(KafkaService.class);

    private KafkaProducer<String, String> producer;

    @Value("${kafka.server.url}")
    public static String kafkaUrl;


    public void createProducer() {

        if (producer == null) {
            // create kafka producer
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CONSTANTS.KAFKA.KAFKA_URL);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            producer = new KafkaProducer<>(props);
        }
    }


    @Override
    public void send(String topic, String data) {
        createProducer();
        try {
            producer.send(new ProducerRecord<>(topic, data));
            System.out.println("=============================");
            log.info("kafka message send.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
