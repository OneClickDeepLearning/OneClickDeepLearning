package com.ocdl.proxy.service;

import com.ocdl.proxy.ProxyCallBack;
import com.ocdl.proxy.domain.Topic;

public interface MessageTransferService {

    public void createConsumer();

    public void consum(Topic topic, ProxyCallBack proxyCallBack);

    public void createProducer();

    public void send(String topic, String data);
}
