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

sudo ./docker_install.sh && sudo ./kubeadm_install.sh $1

echo "Installation complete. Please remember the token and ca certificate hash!"
echo "If you have any questions, please visit https://kubernetes.io/docs/setup/production-environment/tools/kubeadm/install-kubeadm/"

