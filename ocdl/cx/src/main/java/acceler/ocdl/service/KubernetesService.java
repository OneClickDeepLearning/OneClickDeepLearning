package acceler.ocdl.service;

import acceler.ocdl.exception.HdfsException;
import acceler.ocdl.exception.KuberneteException;
import acceler.ocdl.model.InnerUser;

public interface KubernetesService {

    String launchGpuContainer(InnerUser innerUser) throws KuberneteException, HdfsException;

    String launchCpuContainer(InnerUser innerUser) throws KuberneteException, HdfsException;

    void releaseDockerContainer(InnerUser innerUser) throws KuberneteException;
}
