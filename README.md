
<p align="center">
  <br>
 <img src="https://raw.githubusercontent.com/BoyuHuo/OCDL-Image/master/logo_181206.png" alt="Markdownify" width="350" style="border:0px;">
 
<p align="center">
  <a href="#introduction">Introduction</a> •
  <a href="#architecture">Architecture</a> •
    <a href="#architecture">Capsul</a> •
      <a href="#architecture">Rover</a> •
        <a href="#architecture">Launcher</a> •
        <a href="#experiment-cost-estimation">Cost Estimation</a> •
		  <a href="#getting-started">Getting started</a> 
</p>



![license](https://img.shields.io/github/tag-pre/BoyuHuo/OCDL-Image.svg) ![license](https://img.shields.io/badge/license-Apache2.0-blue.svg) ![license](https://img.shields.io/docker/pulls/wbq1995/app.svg)  ![license](https://img.shields.io/github/release-date/Tann-chen/OneClickDLTemp.svg)  



## Introduction

OCDL (One Click Deep Learning)  is an open-source suite of tools for composing connections of models, data, hybrid clouds, computers and devices for deep learning. It builds a bridge between abundant models, data, computing infrastructure, and software platforms which enable users accelerate their completion, testing and deployment of Deep Learning project.

<p align="center">
 <img src="https://raw.githubusercontent.com/BoyuHuo/OCDL-Image/master/model_lifecycle.png" alt="Markdownify" width="700" style="border:0px;">
 </p>

The entire tool set consist three parts:
- **Capsule** 
Capsule is a lightweight web application which offered all the portals such as model development, centralized configuration for the whole environment. It can be considered as the jupyter notebook with tons of extensions for deep learning project.

- **Rover** 
Rover is a middleware which provides distributed computing resources and distributed file system for OCDL. Once Rover receives resource request from Capsule, it will assign a CPU or GPU resource according to the requested resource type. User could upload data to our distributed file system and access to it at memory speed.

- **Launcher** 
Launcher builds a workflow from mature models, which automatically publish model to the thousands of clients. As long as Users correctly set up the Git Repository URL in the Project configuration, publish a model will be just ”One click”!



## Architecture

<p align="center">
 <img src="https://raw.githubusercontent.com/BoyuHuo/OCDL-Image/master/Technology%20%20Stack.png" alt="Markdownify" width="700" style="border:0px;">
 </p>It builds a workflow from trained models, which automatically publish model to the thousands of clients

## Capsule
- Facilitate the process of model development
Capsule embedding the Jupiter IDE into the website as well  as the code templates which contains most popular code  samples such as layers, neural network and networks. So it  boosts the whole process of model development of senior  developer and also reduces the learning curve of junior developers.

- Centralized and Customized to adopt different projects and environment
Capsule allows you to customize your project architecture  by combining it with OCDL Rover and Launcher and cooperate  with them to present a better machine learning  environment. The only thing you need to do for setting  up the whole environment is to finish your configuration through a centralized configuration portal.

- Flexible, efficient and stateless
The OCDL Capsule is a lightweight Java web applicationwhich means the only environment requirement is JVM.There is no reliable plugins, no database and even noconfiguration file in the Capsule. What’s more, the Capsuleis also very flexible, since it supports you to deploy multiple Capsules in one Rover and Launcher which could makeyour resource more reusable.

## Rover
- Application containerization
Our applications such as Jupyter Notebook, is containerized with docker. This means our application can be deployed more rapidly, easily, safely and platform independently.

-  Cross cloud resources integration
Our computing resources are Heterogeneous. For example, our CPU and GPU clusters can be composed of Amazon EC2 instances, Azure instances or even local machines.
Cross cloud resources integration enables us to make full use of our computing resources and enhance resource scalability.

- Clusters management
Our containerised applications on GPU and CPU clusters are deployed, scaled and managed by Kubernetes. Kubernetes provides advanced scheduler to launch container on
cluster nodes automatically. It also provides self healing capabilities which can reschedule, replace and restart the dead containers. These features make our clusters more robust and efficient.

- File system
We provide Hadoop HDFS as underlying file system, allowing users to store large files. HDFS itself is robust and scalable. Since it can be inefficient for HDFS to access data, we use Alluxio on top of HDFS, which enables us to access data at memory speed.

## Launcher
- Model Center
Model Center is a model management module that we designed ourselves. In Model Center, Manager could easily approve or reject a model that submitted by developer. Once the manager clicked the button “APPROVE”, it will start the process of automated model deployment. We also allow Manager to customize their own Algorithm (The different categories’ model they want to publish), and manager could publish the model to a specific Algorithm, and the system will auto assign a version according the manager’s choice.

-  Automated Deployment
The implementation of Auto Deployment like a pipe lines. The physical model file will be pushed to Git Repo after the Manager approve it, then it will be push again to the S3 and waiting the Client to download. Between these movement, Jenkins and Kafka play important connection roles. When model is pushed to Git Repo, it will trigger Jenkins and send the massage to push the model to S3, after model upload to S3, another Kafka message that including model download URL will send to the Client. We also trying to add a testing tool at the Jenkins, before uploading the model to S3, but it will be the future effort. Let’s combinate these two part and think about the process of model publishing: as long as you correctly set the Git Repository URL in the Project configuration, publish a model will be just ”One click”!


## Experiment Cost Estimation
<p align="center">
 <img src="https://raw.githubusercontent.com/BoyuHuo/OCDL-Image/master/hdfs_cluster.png" alt="Markdownify" width="350" style="border:0px;">



## Getting Started
### Prerequisites and Supported Browsers

### Capsule

### Rover

### Launcher

## Sample projects
- NLP for Metropolitan Residence Request Classification
<p align="center">
 <img src="https://raw.githubusercontent.com/BoyuHuo/OCDL-Image/master/nlp1.jpg" alt="Markdownify" width="400" style="border:0px;">
 </p>

- Medical Image Lesion Segmentation
 <p align="center">
 <img src="https://raw.githubusercontent.com/BoyuHuo/OCDL-Image/master/medical2.png" alt="Markdownify" width="400" style="border:0px;">
 </p>
 
- Time Series Forecasting for Cloud Service Workload
 <p align="center">
 <img src="https://raw.githubusercontent.com/BoyuHuo/OCDL-Image/master/timeseries2.jpg" alt="Markdownify" width="400" style="border:0px;">
 </p>



##  Development
### Contributing

-   If you are a new contributor see:  [Steps to Contribute](https://github.com/prometheus/prometheus/blob/master/CONTRIBUTING.md#steps-to-contribute)
    
-   If you have a trivial fix or improvement, go ahead and create a pull request, addressing (with  `@...`) a suitable maintainer of this repository (see  [MAINTAINERS.md](https://github.com/prometheus/prometheus/blob/master/MAINTAINERS.md)) in the description of the pull request.
    
-   If you plan to do something more involved, first discuss your ideas on our  [mailing list](https://groups.google.com/forum/?fromgroups#!forum/prometheus-developers). This will avoid unnecessary work and surely give you and us a good deal of inspiration. Also please see our  [non-goals issue](https://github.com/prometheus/docs/issues/149)  on areas that the Prometheus community doesn't plan to work on.
    
-   Relevant coding style guidelines are the  [Go Code Review Comments](https://code.google.com/p/go-wiki/wiki/CodeReviewComments)  and the  _Formatting and style_  section of Peter Bourgon's  [Go: Best Practices for Production Environments](https://peter.bourgon.org/go-in-production/#formatting-and-style).
    


### Team
Principle investigator
- Yan Liu, yan.liu@concordia.ca

Developers
- Tianen  Chen, email@concordia.ca
- Baiyu  Huo, email@concordia.ca
- Boqian  Wang, email@concordia.ca
- Ivy  Ling, email@concordia.ca

Data scientists
- Jincheng  Su, email@concordia.ca
- Huazhi Liu, email@concordia.ca
- Yushi  Jing, email@concordia.ca

## Userful Link
- <a href="https://kafka.apache.org/quickstart">Kafka Setting</a> 


Refer to [CONTRIBUTING.md](https://github.com/prometheus/prometheus/blob/master/CONTRIBUTING.md)
