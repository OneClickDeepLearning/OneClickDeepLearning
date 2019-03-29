package acceler.ocdl.service;

import acceler.ocdl.exception.KuberneteException;
import acceler.ocdl.model.ResourceType;
import acceler.ocdl.model.User;

public interface KubernetesService {

    String launchGpuContainer(User user) throws KuberneteException;

    String launchCpuContainer(User user) throws KuberneteException;

    void releaseDockerContainer(User user) throws KuberneteException;

}
