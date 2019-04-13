package acceler.ocdl.service;

import acceler.ocdl.model.Project;

public interface ProjectService {

    Project updateProjectConfiguration(Project updatedProjectInfo);

    Project getProjectConfiguration();
}
