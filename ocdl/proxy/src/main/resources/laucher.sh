#!/bin/bash

sudo service httpd start

echo $1

cd /home/git
sudo mkdir $1
sudo chown -R apache:apache $1

cd $1
sudo git init --bare --shared
sudo git config --file config http.receivepack true

cd hooks
sudo mv post-update.sample post-update
sudo sed -i '$d' post-update
sudo bash -c "echo curl http://54.89.140.122:8081/job/$1/build?token=admin >> post-update"

