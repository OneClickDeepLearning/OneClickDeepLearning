package acceler.ocdl.persistence.crud;

import acceler.ocdl.model.Project;

public interface ProjectCrud {

    Project createProject(Project projectInfo);

    Project updateProjct(Long id, Project updatedProjectInfo);

    Project updateProjectName(Long id, Project updatedProjectInfo);

    Project fineById(Long id);
}
