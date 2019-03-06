package acceler.ocdl.service;

import acceler.ocdl.exception.KuberneteException;

public interface KuberneteService {

    String launchDockerContainer(Long projectId, Long userId) throws KuberneteException;

    void releaseDockerContainer(Long projectId, Long userId) throws KuberneteException;
}
