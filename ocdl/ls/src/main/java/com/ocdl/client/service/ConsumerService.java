package com.ocdl.client.service;

import com.ocdl.client.Client;

public interface ConsumerService {

    void createConsumer();

    void run(Client client);
}
