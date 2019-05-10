package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.exception.HdfsException;
import acceler.ocdl.exception.KuberneteException;
import acceler.ocdl.model.AbstractUser;
import acceler.ocdl.model.Project;
import acceler.ocdl.service.HdfsService;
import acceler.ocdl.service.KubernetesService;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Map<Long, String> cpuAssigned = new ConcurrentHashMap<>();
    private static final Map<Long, String> gpuAssigned = new ConcurrentHashMap<>();
    private static final Map<String, String> ipMap = new HashMap<String, String>() {
        {
            put(CONSTANTS.IP.VIRTUAL.MASTER, CONSTANTS.IP.PUBLIC.MASTER);
            put(CONSTANTS.IP.VIRTUAL.CPU, CONSTANTS.IP.PUBLIC.CPU);
            put(CONSTANTS.IP.VIRTUAL.GPU, CONSTANTS.IP.PUBLIC.GPU);
        }
    };

    private final KubernetesClient client = new DefaultKubernetesClient(new ConfigBuilder().withMasterUrl("https://" + CONSTANTS.IP.VIRTUAL.MASTER + ":6443").build());


    private String getUserSpace(AbstractUser user){
        return (CONSTANTS.NAME_FORMAT.USER_SPACE.replace("{projectName}", Project.getProjectNameInStorage()).replace("{userId}", String.valueOf(user.getUserId()))).toLowerCase();
    }

    public String launchGpuContainer(AbstractUser user) throws KuberneteException, HdfsException {
        Long userId = user.getUserId();
        if (gpuAssigned.containsKey(userId))
            return gpuAssigned.get(userId);
        else if (gpuAssigned.size() == CONSTANTS.MACHINE.GPU_AMOUNT)
            throw new KuberneteException("No more GPU resource!");

        String url;
        String ip;
        String port;

        createGpuDeployment(user);

        io.fabric8.kubernetes.api.model.Service service = createGpuService(user);

        log.debug("Container launched!");

        port = getPort(service);
        ip = ipMap.get(getGpuIp(user));
        url = ip + ":" + port;

        if (ip == null || port == null) {
            throw new KuberneteException("Container url unreachable, please try again.");
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
     * @throws KuberneteException
     * @throws HdfsException
     */
    public String launchCpuContainer(AbstractUser user) throws KuberneteException, HdfsException {
        Long userId = user.getUserId();
        if (cpuAssigned.containsKey(userId))
            return cpuAssigned.get(userId);

        String url;
        String ip;
        String port;

        createCpuDeployment(user);

        io.fabric8.kubernetes.api.model.Service service = createCpuService(user);

        log.debug("Container launched!");

        port = getPort(service);
        ip = ipMap.get(getCpuIp(user));
        url = ip + ":" + port;

        //get ip or port before container is really launched
        if (ip == null || port == null) {
            throw new KuberneteException("Container url unreachable, please try again.");
        }

        cpuAssigned.put(userId, url);

        log.debug("container address: " + url);

        return url;
    }

    private Deployment createCpuDeployment(AbstractUser user) {
        String depolyId = getUserSpace(user);
        Deployment deployment = new DeploymentBuilder()
                .withApiVersion("apps/v1")
                .withKind("Deployment")
                .withNewMetadata()
                .withName(depolyId + "-deploy-cpu")
                .addToLabels("app", "cpu1")
                .endMetadata()
                .withNewSpec()
                .withReplicas(1)
                .withNewSelector()
                .addToMatchLabels("app", "cpu1")
                .endSelector()
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels("app", "cpu1")
                .endMetadata()
                .withNewSpec()
                .withNodeName(CONSTANTS.IP.VIRTUAL.GPU)//GPU node address
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

                .addToVolumes()
                .addNewVolume()
                .withName("model")
                .withNewNfs()
                .withServer(CONSTANTS.IP.PUBLIC.MASTER)
                .withPath("/home/hadoop/mount/UserSpace/" + depolyId)
                .endNfs()
                .endVolume()

                .endSpec()
                .endTemplate()
                .endSpec()
                .build();

        try {
            deployment = client.apps().deployments().inNamespace("default").create(deployment);
        } catch (KubernetesClientException e) {
            throw new KuberneteException(e.getMessage());
        }
        return deployment;
    }

    private Deployment createGpuDeployment(AbstractUser user) {
        String depolyId = getUserSpace(user);
        Deployment deployment = new DeploymentBuilder()
                .withApiVersion("apps/v1")
                .withKind("Deployment")
                .withNewMetadata()
                .withName(depolyId + "-deploy-gpu")
                .addToLabels("app", "gpu1")
                .endMetadata()
                .withNewSpec()
                .withReplicas(1)
                .withNewSelector()
                .addToMatchLabels("app", "gpu1")
                .endSelector()
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels("app", "gpu1")
                .endMetadata()
                .withNewSpec()
                .withNodeName(CONSTANTS.IP.VIRTUAL.GPU)//GPU node address
                .addNewContainer()
                .withName("jupyter" + depolyId)
                .withImage("app:gpu")
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

                .addToVolumes()
                .addNewVolume()
                .withName("model")
                .withNewNfs()
                .withServer(CONSTANTS.IP.PUBLIC.MASTER)
                .withPath("/home/hadoop/mount/UserSpace/" + depolyId)
                .endNfs()
                .endVolume()

                .endSpec()
                .endTemplate()
                .endSpec()
                .build();

        try {
            deployment = client.apps().deployments().inNamespace("default").create(deployment);
        } catch (KubernetesClientException e) {
            throw new KuberneteException(e.getMessage());
        }
        return deployment;
    }

    private io.fabric8.kubernetes.api.model.Service createCpuService(AbstractUser user) {
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
                .withSelector(Collections.singletonMap("app", "cpu1"))
                .endSpec()
                .build();

        try {
            service = client.services().inNamespace("default").create(service);
        } catch (KubernetesClientException e) {
            throw new KuberneteException(e.getMessage());
        }
        return service;
    }

    private io.fabric8.kubernetes.api.model.Service createGpuService(AbstractUser user) {
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
                .withSelector(Collections.singletonMap("app", "gpu1"))
                .endSpec()
                .build();

        try {
            service = client.services().inNamespace("default").create(service);
        } catch (KubernetesClientException e) {
            throw new KuberneteException(e.getMessage());
        }
        return service;
    }

    public String getGpuIp(AbstractUser user) {
        StringBuilder podId = new StringBuilder();
        String userSpace = getUserSpace(user);
        podId.append(userSpace).append("-deploy-gpu");

        for (Pod pod : client.pods().inNamespace("default").list().getItems()) {
            if (pod.getMetadata().getName().contains(podId.toString())) {
                return pod.getStatus().getHostIP();
            }
        }

        throw new KuberneteException("Can not find container's IP address");
    }

    public String getCpuIp(AbstractUser user) {

        StringBuilder podId = new StringBuilder();

        String userSpace = getUserSpace(user);
        podId.append(userSpace).append("-deploy-cpu");

        for (Pod pod : client.pods().inNamespace("default").list().getItems()) {
            if (pod.getMetadata().getName().contains(podId.toString())) {
                return pod.getStatus().getHostIP();
            }
        }

        throw new KuberneteException("Can not find container's IP address");
    }

    private String getPort(io.fabric8.kubernetes.api.model.Service service) {

        String port;
        try {
            port = service.getSpec().getPorts().get(0).getNodePort().toString();
        } catch (KubernetesClientException e) {
            throw new KuberneteException(e.getMessage());
        }
        return port;
    }

    public void releaseDockerContainer(AbstractUser user) throws KuberneteException {

        String userId = getUserSpace(user);
        try {
            for (io.fabric8.kubernetes.api.model.Service svc : client.services().inNamespace("default").list().getItems()) {
                System.out.println(svc.getMetadata().getName());

                if (svc.getMetadata().getName().contains(userId))
                    client.resource(svc).delete();
            }
            for (Deployment deploy : client.apps().deployments().inNamespace("default").list().getItems()) {
                System.out.println(deploy.getMetadata().getName());
                if (deploy.getMetadata().getName().contains(userId))
                    client.resource(deploy).delete();
            }

            //release resource cache
            if(cpuAssigned.containsKey(userId)){
                cpuAssigned.remove(userId);
            }
            if(gpuAssigned.containsKey(userId)){
                gpuAssigned.remove(userId);
            }

        } catch (KubernetesClientException e) {
            throw new KuberneteException(e.getMessage());
        }
    }
}
