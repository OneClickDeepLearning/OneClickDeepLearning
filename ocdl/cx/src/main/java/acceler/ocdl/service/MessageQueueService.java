package acceler.ocdl.service;

public interface MessageQueueService {

    void addTopic(String topic);

    void send(String topic, String data);
}
