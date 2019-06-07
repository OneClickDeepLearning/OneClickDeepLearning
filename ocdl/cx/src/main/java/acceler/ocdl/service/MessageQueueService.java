package acceler.ocdl.service;

public interface MessageQueueService {

    void send(String topic, String data);
}
