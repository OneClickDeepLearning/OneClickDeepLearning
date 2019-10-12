#!/bin/bash

#install java
sudo apt update
sudo apt install -y default-jre
sudo apt install -y default-jdk
echo "java installed"

#download kafka
mkdir /home/ubuntu/kafka
cd kafka
wget http://apache.mirror.iweb.ca/kafka/2.3.0/kafka_2.12-2.3.0.tgz
tar -xzf kafka_2.12-2.3.0.tgz 
cd kafka_2.12-2.3.0/
sudo chown ubuntu:ubuntu logs/
sudo chown ubuntu:ubuntu logs/*.*
echo "kafka installed"

# update config file of kafka server
DNS="ec2-52-90-120-157.compute-1.amazonaws.com"
echo -e '\n\n\n' >> config/server.properties
echo "listeners=PLAINTEXT://${DNS}:9092" >> config/server.properties

# replace boot automatic file
sudo mv /home/ubuntu/kafka/zookeeper.service /etc/systemd/system/zookeeper.service
sudo mv /home/ubuntu/kafka/kafka.service /etc/systemd/system/kafka.service

sudo systemctl start kafka