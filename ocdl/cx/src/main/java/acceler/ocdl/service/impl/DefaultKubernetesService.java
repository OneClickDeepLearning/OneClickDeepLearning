package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.entity.User;
import acceler.ocdl.exception.KubernetesException;
import acceler.ocdl.service.HdfsService;
import acceler.ocdl.service.KubernetesService;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.fabric8.kubernetes.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * KubernetesService is responsible for launching and releasing
 * CPU or GPU container based on user information
 */
@Service
public class DefaultKubernetesService implements KubernetesService {
    Logger log = LoggerFactory.getLogger(DefaultKubernetesService.class);

    @Autowired
    private HdfsService hdfsService;

    @Value("${APPLICATIONS_DIR.USER_SPACE}")
    private String applicationsDirUserSpace;

    private static String k8sVirtualMasterIp;
    private static String k8sPublicMasterIp;
    private static String k8sVirtualCpu01Ip;
    private static String k8sPublicCpu01Ip;
    private static String k8sVirtualCpu02Ip;
    private static String k8sPublicCpu02Ip;
    private static String k8sVirtualGpu03Ip;
    private static String k8sPublicGpu03Ip;

    private static final Map<Long, String> cpuAssigned = new ConcurrentHashMap<>();
    private static final Map<Long, String> gpuAssigned = new ConcurrentHashMap<>();
    private final Map<String, String> ipMap = new HashMap<String, String>();

    private void initIpMap(){
        ipMap.put(k8sVirtualMasterIp, k8sPublicMasterIp);
        ipMap.put(k8sVirtualCpu01Ip, k8sPublicCpu01Ip);
        ipMap.put(k8sVirtualCpu02Ip, k8sPublicCpu02Ip);
        ipMap.put(k8sVirtualGpu03Ip, k8sPublicGpu03Ip);

        client = new DefaultKubernetesClient(new ConfigBuilder().withMasterUrl("https://"+k8sVirtualMasterIp+":6443").build());
    }

    private KubernetesClient client ;

    private String getUserSpace(User user){
        return (CONSTANTS.NAME_FORMAT.USER_SPACE.replace("{userId}", String.valueOf(user.getId()))).toLowerCase();
    }

    public String launchGpuContainer(User user) throws KubernetesException {

        if(!ipMap.containsKey(k8sVirtualMasterIp)){
            initIpMap();
        }

        Long userId = user.getId();
        if (gpuAssigned.containsKey(userId))
            return gpuAssigned.get(userId);

        String url;
        String ip;
        String port;

        createGpuDeployment(user);

        io.fabric8.kubernetes.api.model.Service service = createGpuService(user);

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        log.debug("Container launched!");

        port = getPort(service);
        ip = ipMap.get(getGpuIp(user));
        url = ip + ":" + port;

        if (ip == null || port == null) {
            throw new KubernetesException("Container url unreachable, please try again.");
        }

        gpuAssigned.put(userId, url);
        log.debug(url);
        return url;
    }


    /**
     * launch a CPU container for user
     *
     * @param user userId is required to determine corresponding userspace
     * @return url the address of the launched container format is ip:port
     * @throws KubernetesException
     */
    public String launchCpuContainer(User user) throws KubernetesException {

        if(!ipMap.containsKey(k8sVirtualMasterIp)){
            initIpMap();
        }

        Long userId = user.getId();
        if (cpuAssigned.containsKey(userId))
            return cpuAssigned.get(userId);

        String url;
        String ip;
        String port;

        createCpuDeployment(user);

        io.fabric8.kubernetes.api.model.Service service = createCpuService(user);

        try{
        Thread.sleep(1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        log.debug("Container launched!");

        port = getPort(service);
        ip = ipMap.get(getCpuIp(user));
        url = ip + ":" + port;

        //get ip or port before container is really launched
        if (ip == null || port == null) {
            throw new KubernetesException("Container url unreachable, please try again.");
        }

        cpuAssigned.put(userId, url);

        log.debug("container address: " + url);

        return url;
    }

    private Deployment createCpuDeployment(User user) {
        String depolyId = getUserSpace(user);

        Deployment deployment = new DeploymentBuilder()
                .withApiVersion("apps/v1")
                .withKind("Deployment")
                .withNewMetadata()
                .withName(depolyId + "-deploy-cpu")
                .addToLabels("app", depolyId + "-deploy-cpu")
                .endMetadata()
                .withNewSpec()
                .withReplicas(1)
                .withNewSelector()
                .addToMatchLabels("app", depolyId + "-deploy-cpu")
                .endSelector()
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels("app", depolyId + "-deploy-cpu")
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

                .addToVolumeMounts()
                .addNewVolumeMount()
                .withMountPath("/root/Model")
                .withName("model")
                .endVolumeMount()
                .withImagePullPolicy("Never")
                .endContainer()

                .withNewDnsConfig()
                .withNameservers("8.8.8.8")
                .endDnsConfig()

                .withNodeSelector(new HashMap<String, String>(){{
                                      put("rctype","cpu");
                                  }}
                )

                .addToVolumes()
                .addNewVolume()
                .withName("model")
                .withNewNfs()
                .withServer(k8sVirtualMasterIp)
                .withPath(applicationsDirUserSpace + depolyId)
                .endNfs()
                .endVolume()

                .endSpec()
                .endTemplate()
                .endSpec()
                .build();

        try {
          deployment = client.apps().deployments().inNamespace("default").create(deployment);
        } catch (KubernetesClientException e) {
            throw new KubernetesException(e.getMessage());
        }
        return deployment;
    }

    private Deployment createGpuDeployment(User user) {
        String depolyId = getUserSpace(user);
        Deployment deployment = new DeploymentBuilder()
                .withApiVersion("apps/v1")
                .withKind("Deployment")
                .withNewMetadata()
                .withName(depolyId + "-deploy-gpu")
                .addToLabels("app", depolyId + "-deploy-gpu")
                .endMetadata()
                .withNewSpec()
                .withReplicas(1)
                .withNewSelector()
                .addToMatchLabels("app", depolyId + "-deploy-gpu")
                .endSelector()
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels("app", depolyId + "-deploy-gpu")
                .endMetadata()
                .withNewSpec()
                //.withNodeName(CONSTANTS.IP.VIRTUAL.GPU)//GPU node address
                .addNewContainer()
                .withName("jupyter" + depolyId)
                .withImage("app:gpu")

                .withNewSecurityContext()
                .withPrivileged(true)
                .endSecurityContext()

                .addNewPort()
                .withContainerPort(8998)
                .endPort()


                .withStdin(true)
                .withTty(true)
                .withWorkingDir("/root")

                .addToVolumeMounts()
                .addNewVolumeMount()
                .withMountPath("/root/Model")
                .withName("model")
                .endVolumeMount()
                .withImagePullPolicy("Never")
                .endContainer()

                .withNewDnsConfig()
                .withNameservers("8.8.8.8")
                .endDnsConfig()

                .withNodeSelector(new HashMap<String, String>(){{
                    put("rctype","gpu");
                }}
                )

                .addToVolumes()
                .addNewVolume()
                .withName("model")
                .withNewNfs()
                .withServer(k8sVirtualMasterIp)
                .withPath(applicationsDirUserSpace + depolyId)
                .endNfs()
                .endVolume()

                .endSpec()
                .endTemplate()
                .endSpec()
                .build();

        try {
            deployment = client.apps().deployments().inNamespace("default").create(deployment);
        } catch (KubernetesClientException e) {
            throw new KubernetesException(e.getMessage());
        }
        return deployment;
    }

    private io.fabric8.kubernetes.api.model.Service createCpuService(User user) {
        String svcId = getUserSpace(user);
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
                .withSelector(Collections.singletonMap("app", svcId + "-deploy-cpu"))
                .endSpec()
                .build();

        try {
            service = client.services().inNamespace("default").create(service);
        } catch (KubernetesClientException e) {
            throw new KubernetesException(e.getMessage());
        }
        return service;
    }

    private io.fabric8.kubernetes.api.model.Service createGpuService(User user) {
        String svcId = getUserSpace(user);
        io.fabric8.kubernetes.api.model.Service service = new ServiceBuilder()
                .withApiVersion("v1")
                .withKind("Service")
                .withNewMetadata()
                .withName("svc-" + svcId + "-gpu")
                .endMetadata()
                .withNewSpec()
                .withType("NodePort")
                .addNewPort()
                .withPort(8998)
                .withNewTargetPort(8998)
                .withProtocol("TCP")
                .endPort()
                .withSelector(Collections.singletonMap("app", svcId + "-deploy-gpu"))
                .endSpec()
                .build();

        try {
            service = client.services().inNamespace("default").create(service);
        } catch (KubernetesClientException e) {
            throw new KubernetesException(e.getMessage());
        }
        return service;
    }

    public String getGpuIp(User user) {
        StringBuilder podId = new StringBuilder();
        String userSpace = getUserSpace(user);
        podId.append(userSpace).append("-deploy-gpu");

        for (Pod pod : client.pods().inNamespace("default").list().getItems()) {
            if (pod.getMetadata().getName().contains(podId.toString())) {
                return pod.getStatus().getHostIP();
            }
        }

        throw new KubernetesException("Can not find container's IP address");
    }

    public String getCpuIp(User user) {

        StringBuilder podId = new StringBuilder();

        String userSpace = getUserSpace(user);
        podId.append(userSpace).append("-deploy-cpu");

        for (Pod pod : client.pods().inNamespace("default").list().getItems()) {
            if (pod.getMetadata().getName().contains(podId.toString())) {
                return pod.getStatus().getHostIP();
            }
        }

        throw new KubernetesException("Can not find container's IP address");
    }

    private String getPort(io.fabric8.kubernetes.api.model.Service service) {

        String port;
        try {
            port = service.getSpec().getPorts().get(0).getNodePort().toString();
        } catch (KubernetesClientException e) {
            throw new KubernetesException(e.getMessage());
        }
        return port;
    }

    public void releaseDockerContainer(User user) throws KubernetesException {

        String userId = getUserSpace(user);
        try {
            if(client.services().inNamespace("default").list().getItems().size() > 0) {
                for (io.fabric8.kubernetes.api.model.Service svc : client.services().inNamespace("default").list().getItems()) {
                    System.out.println(svc.getMetadata().getName());

                    if (svc.getMetadata().getName().contains(userId))
                        client.resource(svc).delete();
                }
            }

            if (client.apps().deployments().inNamespace("default").list().getItems().size() > 0){
                for (Deployment deploy : client.apps().deployments().inNamespace("default").list().getItems()) {
                    System.out.println(deploy.getMetadata().getName());
                    if (deploy.getMetadata().getName().contains(userId))
                        client.resource(deploy).delete();
                }
            }

            if (client.apps().replicaSets().inNamespace("default").list().getItems().size() > 0) {
                for(ReplicaSet replicaSet : client.apps().replicaSets().inNamespace("default").list().getItems()) {
                    System.out.println(replicaSet.getMetadata().getName());
                    if (replicaSet.getMetadata().getName().contains(userId))
                        client.resource(replicaSet).delete();
                }
            }

            if (client.pods().inNamespace("default").list().getItems().size() > 0) {
                for (Pod pod : client.pods().inNamespace("default").list().getItems()){
                    System.out.println(pod.getMetadata().getName());
                    if(pod.getMetadata().getName().contains(userId))
                        client.resource(pod).delete();
                }
            }

            //release resource cache
            if(cpuAssigned.containsKey(user.getId())){
                cpuAssigned.remove(user.getId());
            }
            if(gpuAssigned.containsKey(user.getId())){
                gpuAssigned.remove(user.getId());
            }

        } catch (KubernetesClientException e) {
            throw new KubernetesException(e.getMessage());
        }
    }


    @Value("${K8S.VIRTUAL.MASTER}")
    public void setK8sVirtualMasterIp(String k8sVirtualMasterIp){
        this.k8sVirtualMasterIp = k8sVirtualMasterIp;
    }

    @Value("${K8S.PUBLIC.MASTER}")
    public void setK8sPublicMasterIp(String k8sPublicMasterIp){
        this.k8sPublicMasterIp = k8sPublicMasterIp;
    }
    @Value("${K8S.VIRTUAL.CPU01}")
    public void setK8sVirtualCpu01Ip(String k8sVirtualCpu01Ip){
        this.k8sVirtualCpu01Ip = k8sVirtualCpu01Ip;
    }
    @Value("${K8S.PUBLIC.CPU01}")
    public void setK8sPublicCpu01Ip(String k8sPublicCpu01Ip){
        this.k8sPublicCpu01Ip = k8sPublicCpu01Ip;
    }
    @Value("${K8S.VIRTUAL.CPU02}")
    public void setK8sVirtualCpu02Ip(String k8sVirtualCpu02Ip){
        this.k8sVirtualCpu02Ip = k8sVirtualCpu02Ip;
    }
    @Value("${K8S.PUBLIC.CPU02}")
    public void setK8sPublicCpu02Ip(String k8sPublicCpu02Ip){
        this.k8sPublicCpu02Ip = k8sPublicCpu02Ip;
    }
    @Value("${K8S.VIRTUAL.GPU03}")
    public void setK8sVirtualGpu03Ip(String k8sVirtualGpu03Ip){
        this.k8sVirtualGpu03Ip = k8sVirtualGpu03Ip;
    }
    @Value("${K8S.PUBLIC.GPU03}")
    public void setK8sPublicGpu03Ip(String k8sPublicGpu03Ip){
        this.k8sPublicGpu03Ip = k8sPublicGpu03Ip;
    }
}
