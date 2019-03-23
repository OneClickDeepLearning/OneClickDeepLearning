#!/bin/bash

cd /var/lib/kafka_2.11-2.1.0/

sudo bin/kafka-topics.sh --create --zookeeper ec2-54-89-140-122.compute-1.amazonaws.com:2181 --replication-factor 1 --partitions 1 --topic $1