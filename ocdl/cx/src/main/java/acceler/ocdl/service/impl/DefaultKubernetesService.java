package acceler.ocdl.service.impl;

import acceler.ocdl.exception.KuberneteException;
import acceler.ocdl.model.ResourceType;
import acceler.ocdl.model.User;
import acceler.ocdl.persistence.ProjectCrud;
import acceler.ocdl.service.KubernetesService;
import acceler.ocdl.utils.CmdHelper;
import acceler.ocdl.utils.impl.DefaultCmdHelper;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultKubernetesService implements KubernetesService {

    @Autowired
    private ProjectCrud projectCrud;

    private static final Map<Long, String> cpuAssigned = new ConcurrentHashMap<>();
    private static final Map<Long, String> gpuAssigned = new ConcurrentHashMap<>();
    private static final Map<String,String> ipMap = new HashMap<String, String>(){
        {
            put("10.8.0.1", "3.89.28.106");
            put("10.8.0.6", "3.87.64.159");
            put("10.8.0.10", "66.131.186.246");
        }
    };

    private final KubernetesClient client = new DefaultKubernetesClient(new ConfigBuilder().withMasterUrl("https://10.8.0.1:6443").build());


    public String launchGpuContainer(User user) throws KuberneteException{
        Long userId = user.getUserId();
        if(gpuAssigned.containsKey(userId))
            return gpuAssigned.get(userId);
        else if(gpuAssigned.size() == 1)
            throw new KuberneteException("No more GPU resource!");

        String url;
        String ip;
        String port;

        Deployment deployment = createGpuDeployment(user);
        io.fabric8.kubernetes.api.model.Service service = createService(user);

        System.out.println("[dubug] " + "Container launched!");

        port = getPort(service);
        ip = ipMap.get(getGpuIp(user));
        url = ip + ":" + port;
        gpuAssigned.put(userId,url);
        System.out.println("[dubug] " + url);
        return url;
    }

    public String launchCpuContainer(User user) throws KuberneteException{

        Long userId = user.getUserId();
        if(cpuAssigned.containsKey(userId))
            return cpuAssigned.get(userId);

        String url;
        String ip;
        String port;

        Deployment deployment = createCpuDeployment(user);
        io.fabric8.kubernetes.api.model.Service service = createService(user);

        System.out.println("[dubug] " + "Container launched!");

        port = getPort(service);
        ip = ipMap.get(getCpuIp(user));
        url = ip + ":" + port;
        cpuAssigned.put(userId,url);
        System.out.println("[dubug] " + url);
        return url;
    }

    public void releaseDockerContainer(ResourceType rscType, User user) throws KuberneteException{

    }

    private Deployment createCpuDeployment(User user){

        String depolyId = projectCrud.getProjectName() + "-" + user.getUserId().toString();

        Deployment deployment = new DeploymentBuilder()
                .withApiVersion("apps/v1")
                .withKind("Deployment")
                .withNewMetadata()
                .withName(depolyId + "-deploy-cpu")
                .addToLabels("app","cpu1")
                .endMetadata()
                .withNewSpec()
                .withReplicas(1)
                .withNewSelector()
                .addToMatchLabels("app","cpu1")
                .endSelector()
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels("app","cpu1")
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName("jupyter" + depolyId)
                .withImage("app:cpu")
                .addNewPort()
                .withContainerPort(8998)
                .endPort()
                .withStdin(true)
                .withTty(true)
                .withWorkingDir("/root")
                .addNewVolumeMount()
                .withMountPath("/root/UserSpace")
                .withName("model")
                .withMountPath("/root/CommonDataSets")
                .withName("dataset")
                .withReadOnly(true)
                .endVolumeMount()
                .withImagePullPolicy("Never")
                .endContainer()
                .addNewVolume()
                .withName("model")
                .withNewHostPath()
                .withPath("/home/hadoop/mount/UserSpace/" + depolyId)
                .endHostPath()
                .withName("dataset")
                .withNewHostPath()
                .withPath("/home/hadoop/mount/CommonSpace")
                .endHostPath()
                .endVolume()
                .endSpec()
                .endTemplate()
                .endSpec()
                .build();

        try {
            deployment = client.apps().deployments().inNamespace("default").create(deployment);
        } catch (KubernetesClientException e){
            throw new KuberneteException(e.getMessage());
        }
        return deployment;
    }

    private Deployment createGpuDeployment(User user){

        String depolyId = projectCrud.getProjectName() + "-" + user.getUserId().toString();

        Deployment deployment = new DeploymentBuilder()
                .withApiVersion("apps/v1")
                .withKind("Deployment")
                .withNewMetadata()
                .withName(depolyId + "-deploy-cpu")
                .addToLabels("app","cpu1")
                .endMetadata()
                .withNewSpec()
                .withReplicas(1)
                .withNewSelector()
                .addToMatchLabels("app","cpu1")
                .endSelector()
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels("app","cpu1")
                .endMetadata()
                .withNewSpec()
                .withNodeName("10.8.0.10")//GPU node address
                .addNewContainer()
                .withName("jupyter" + depolyId)
                .withImage("app:cpu")
                .addNewPort()
                .withContainerPort(8998)
                .endPort()
                .withStdin(true)
                .withTty(true)
                .withWorkingDir("/root")
                .addNewVolumeMount()
                .withMountPath("/root/UserSpace")
                .withName("model")
                .withMountPath("/root/CommonDataSets")
                .withName("dataset")
                .withReadOnly(true)
                .endVolumeMount()
                .withImagePullPolicy("Never")
                .endContainer()
                .addNewVolume()
                .withName("model")
                .withNewHostPath()
                .withPath("/home/hadoop/mount/UserSpace/" + depolyId)
                .endHostPath()
                .withName("dataset")
                .withNewHostPath()
                .withPath("/home/hadoop/mount/CommonSpace")
                .endHostPath()
                .endVolume()
                .endSpec()
                .endTemplate()
                .endSpec()
                .build();

        try {
            deployment = client.apps().deployments().inNamespace("default").create(deployment);
        } catch (KubernetesClientException e){
            throw new KuberneteException(e.getMessage());
        }
        return deployment;
    }

    private io.fabric8.kubernetes.api.model.Service createService(User user){

        String svcId = projectCrud.getProjectName() + "-" + user.getUserId().toString();

        io.fabric8.kubernetes.api.model.Service service = new ServiceBuilder()
                .withApiVersion("v1")
                .withKind("Service")
                .withNewMetadata()
                .withName("svc-" + svcId + "-cpu")
                .endMetadata()
                .withNewSpec()
                .withType("NodePort")
                .addNewPort()
                .withPort(8998)
                .withNewTargetPort(8998)
                .withProtocol("TCP")
                .endPort()
                .withSelector(Collections.singletonMap("app","cpu1"))
                .endSpec()
                .build();

        try {
            service = client.services().inNamespace("default").create(service);
        } catch (KubernetesClientException e){
            throw new KuberneteException(e.getMessage());
        }
        return service;
    }

    public String getGpuIp(User user){
        StringBuilder podId = new StringBuilder();
        podId.append(projectCrud.getProjectName()).append("-").append(user.getUserId().toString()).append("-deploy-gpu");


        for(Pod pod : client.pods().inNamespace("default").list().getItems()){
            if(pod.getMetadata().getName().contains(podId.toString())){
                return pod.getStatus().getHostIP();
            }
        }

        throw new KuberneteException("Can not find container's IP address");
    }

    public String getCpuIp(User user){

        StringBuilder podId = new StringBuilder();
        podId.append(projectCrud.getProjectName()).append("-").append(user.getUserId().toString()).append("-deploy-cpu");


        for(Pod pod : client.pods().inNamespace("default").list().getItems()){
            if(pod.getMetadata().getName().contains(podId.toString())){
                return pod.getStatus().getHostIP();
            }
        }

        throw new KuberneteException("Can not find container's IP address");
    }

    private String getPort(io.fabric8.kubernetes.api.model.Service service) {

        String port;
        try {
            port = service.getSpec().getPorts().get(0).getNodePort().toString();
        } catch (KubernetesClientException e){
            throw new KuberneteException(e.getMessage());
        }
        return port;
    }
}
