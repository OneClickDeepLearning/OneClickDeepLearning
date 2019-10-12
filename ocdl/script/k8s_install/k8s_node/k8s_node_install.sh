#!/bin/bash

set -e

trap "exit" INT

echo "
*****************************************************
*             KUBERNETES INSTALLATION               *
*                                                   *
*****************************************************
"

if [[ -z "$1" ]]
then
    echo "Please enter k8s master ip address after the command"
    exit
fi

if [[ -z "$2" ]]
then
    echo "Please enter token after the command"
    exit
fi

if [[ -z "$3" ]]
then
    echo "Please enter ca certificate hash after the command"
    exit
fi



sudo ./docker_install.sh && sudo ./kubeadm_install.sh


sudo kubeadm join $1 --token $2 --discovery-token-ca-cert-hash $3

echo "Installation complete."
