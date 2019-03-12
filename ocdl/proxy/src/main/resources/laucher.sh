#!/bin/bash

sudo service httpd start

new_file_name = $1
echo $new_file_name

cd /home/git
mkdir $new_file_name

cd $new_file_name
git init --bare --shared
git config --file config http.receivepack true

cd hooks
sudo mv post-update.sample post-update
sudo sed -i '$d' post-update
sudo bash -c "echo curl http://54.89.140.122:8081/job/$new_file_name/build?token=ivy >> post-update"

