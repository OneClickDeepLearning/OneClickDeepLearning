package com.ocdl.proxy.service.impl;


import com.ocdl.proxy.service.KafkaTopicService;
import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.kafka.common.KafkaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.*;



@Component
public class DefaultKafkaTopicService implements KafkaTopicService {

    String ZookeeperDNS;
    int sessionTimeoutMs = 10 * 1000;
    int connectionTimeoutMs = 8 * 1000;

    @Value("${zookeeper.server.url}")
    public void setZookeeperDNS(String ZookeeperDNS) { this.ZookeeperDNS = ZookeeperDNS; }

    public DefaultKafkaTopicService() {
    }

    @Override
    public void createTopic(String topic) {

        ZkClient zkClient = new ZkClient(ZookeeperDNS, sessionTimeoutMs, connectionTimeoutMs, ZKStringSerializer$.MODULE$);

        // Security for Kafka was added in Kafka 0.9.0.0
        boolean isSecureKafkaCluster = false;
        ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(ZookeeperDNS), isSecureKafkaCluster);

        int partitions = 1;
        int replication = 1;
        Properties topicConfig = new Properties(); // add per-topic configurations settings here
        AdminUtils.createTopic(zkUtils, topic, partitions, replication, topicConfig);
        zkClient.close();
    }
}
