package acceler.ocdl.service;

import acceler.ocdl.entity.User;
import acceler.ocdl.exception.HdfsException;
import acceler.ocdl.exception.KubernetesException;

public interface KubernetesService {

    String launchGpuContainer(User user) throws KubernetesException, HdfsException;

    String launchCpuContainer(User user) throws KubernetesException, HdfsException;

    void releaseDockerContainer(User user) throws KubernetesException;
}
