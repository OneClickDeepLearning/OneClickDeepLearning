package acceler.ocdl.service.impl;

import acceler.ocdl.dao.PlatformMetaDao;
import acceler.ocdl.entity.PlatformMeta;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.service.MessageQueueService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Properties;

@Component
public class KafkaService implements MessageQueueService {

    private static final Logger log = Logger.getLogger(KafkaService.class);

    @Autowired
    private PlatformMetaDao platformMetaDao;

    private KafkaProducer<String, String> producer;

    public void createProducer() {

        if (producer == null) {
            // create kafka producer
            Properties props = new Properties();
            PlatformMeta meta = platformMetaDao.findById(4L)
                    .orElseThrow(() -> new OcdlException("Fail to get Platform meta data."));
            String kafkaUrl = meta.getKafkaUrl();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            producer = new KafkaProducer<>(props);
        }
    }

    @Override
    public void addTopic(String topic) {
        //TODO add topic in kafka
    }


    @Override
    public void send(String topic, String data) {

        createProducer();
        System.out.println("create producer");

        try {
            producer.send(new ProducerRecord<>(topic, data));
            System.out.println("=============================");
            log.info("kafka message send.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //producer.close();
    }
}
