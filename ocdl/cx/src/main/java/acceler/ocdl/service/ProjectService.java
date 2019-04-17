package acceler.ocdl.service;

import acceler.ocdl.model.Project;

public interface ProjectService {

    /**
     * update project configuration
     * @param updatedProjectInfo the Project obj that contains update info
     * @return updatedProject obj
     */
    Project updateProjectConfiguration(Project updatedProjectInfo);

    /**
     * get Project obj that contains project configuration info
     * @return Project obj
     */
    Project getProjectConfiguration();
}
