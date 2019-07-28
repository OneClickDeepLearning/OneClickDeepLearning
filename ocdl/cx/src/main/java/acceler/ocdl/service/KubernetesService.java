package acceler.ocdl.service;

import acceler.ocdl.exception.HdfsException;
import acceler.ocdl.exception.KubernetesException;
import acceler.ocdl.model.AbstractUser;

public interface KubernetesService {

    String launchGpuContainer(AbstractUser user) throws KubernetesException, HdfsException;

    String launchCpuContainer(AbstractUser user) throws KubernetesException, HdfsException;

    void releaseDockerContainer(AbstractUser user) throws KubernetesException;
}
