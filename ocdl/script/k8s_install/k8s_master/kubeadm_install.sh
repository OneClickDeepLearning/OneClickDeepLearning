#!/bin/bash

#Installing kubeadm on your hosts
sudo apt-get update && sudo apt-get install -y apt-transport-https curl
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add -
cat <<EOF >/etc/apt/sources.list.d/kubernetes.list
deb https://apt.kubernetes.io/ kubernetes-xenial main
EOF
sudo apt-get update &&
sudo apt-get install -y kubelet kubeadm kubectl &&
sudo apt-mark hold kubelet kubeadm kubectl

#Disabling swap
sudo swapoff -a &&

#Initializing your control-plane node
sudo kubeadm init  --apiserver-advertise-address $1 --pod-network-cidr=10.244.0.0/16 &&

mkdir -p $HOME/.kube &&
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config &&
sudo chown $(id -u):$(id -g) $HOME/.kube/config &&

#Passing bridged IPv4 traffic to iptables’ chains
sudo sysctl net.bridge.bridge-nf-call-iptables=1 &&

sudo kubectl taint nodes --all node-role.kubernetes.io/master:NoSchedule- &&

#Installing a pod network add-on
sudo kubectl apply -f kube-flannel.yml
