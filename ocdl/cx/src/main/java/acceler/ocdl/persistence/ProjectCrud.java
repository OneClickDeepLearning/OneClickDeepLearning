package acceler.ocdl.persistence;

import acceler.ocdl.model.Project;

public interface ProjectCrud {

    Project updateProjct(Project updatedProjectInfo);

    Project updateProjectName(String name);

    Project getProjectConfiguration();

    String getProjectName();
}
