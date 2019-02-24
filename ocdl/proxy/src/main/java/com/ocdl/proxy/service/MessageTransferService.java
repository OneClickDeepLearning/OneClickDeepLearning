package com.ocdl.proxy;

import com.ocdl.proxy.domain.Topic;

public interface MessageTransferService {

    public void createConsumer();

    public void consum(Topic topic, Proxy proxy);

    public void createProducer();

    public void send(Topic topic, String data);
}
