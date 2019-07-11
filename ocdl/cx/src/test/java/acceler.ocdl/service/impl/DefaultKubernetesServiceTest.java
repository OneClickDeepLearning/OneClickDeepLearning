package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.exception.KuberneteException;
import acceler.ocdl.service.KubernetesService;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultKubernetesServiceTest {

    @Autowired
    private KubernetesService service;

    @Test
    public void launchDockerContainer() {

        final KubernetesClient client = new DefaultKubernetesClient(new ConfigBuilder().withMasterUrl("https://10.8.0.1':6443").build());

        Deployment deployment = new DeploymentBuilder()
                .withApiVersion("apps/v1")
                .withKind("Deployment")
                .withNewMetadata()
                .withName(1000 + "-deploy-cpu")
                .addToLabels("app", 1000 + "-deploy-cpu")
                .endMetadata()
                .withNewSpec()
                .withReplicas(1)
                .withNewSelector()
                .addToMatchLabels("app", 1000 + "-deploy-cpu")
                .endSelector()
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels("app", 1000 + "-deploy-cpu")
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName("jupyter" + 1000)
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

                .addToVolumes()
                .addNewVolume()
                .withName("model")
                .withNewNfs()
                .withServer(CONSTANTS.IP.PUBLIC.MASTER)
                .withPath("/home/hadoop/mount/UserSpace/" + 1000)
                .endNfs()
                .endVolume()

                .endSpec()
                .endTemplate()
                .endSpec()
                .build();

        try {
            deployment = client.apps().deployments().inNamespace("default").create(deployment);
        } catch (KubernetesClientException e) {
            System.out.println("~~~~~~~EEEEXXXXCCCEEEPPPTTIIOONN");
            throw new KuberneteException(e.getMessage());
        }


    }
}