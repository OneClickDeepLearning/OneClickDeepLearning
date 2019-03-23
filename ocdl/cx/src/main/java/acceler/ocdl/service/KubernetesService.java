package acceler.ocdl.service;

import acceler.ocdl.exception.KuberneteException;
import acceler.ocdl.model.User;

public interface KubernetesService {

    String launchDockerContainer(String rscType, User user) throws KuberneteException;

    void releaseDockerContainer(String rscType, User user) throws KuberneteException;

}
