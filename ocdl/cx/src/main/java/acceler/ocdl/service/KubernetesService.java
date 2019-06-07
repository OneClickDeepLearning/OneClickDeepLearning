package acceler.ocdl.service;

import acceler.ocdl.exception.HdfsException;
import acceler.ocdl.exception.KuberneteException;
import acceler.ocdl.model.AbstractUser;

public interface KubernetesService {

    String launchGpuContainer(AbstractUser user) throws KuberneteException, HdfsException;

    String launchCpuContainer(AbstractUser user) throws KuberneteException, HdfsException;

    void releaseDockerContainer(AbstractUser user) throws KuberneteException;
}
