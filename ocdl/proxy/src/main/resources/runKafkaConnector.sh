#!/bin/bash

cd /var/lib/kafka_2.11-2.1.0/

touch $1

cd config/

sudo sed -i '$d' connect-file-source.properties
sudo sed -i '$d' connect-file-source.properties
sudo bash -c "echo file=$1 >> connect-file-source.properties"
sudo bash -c "echo topic=$2 >> connect-file-source.properties"


cd ..
sudo bin/connect-standalone.sh config/connect-standalone.properties config/connect-file-source.properties > connect.log &