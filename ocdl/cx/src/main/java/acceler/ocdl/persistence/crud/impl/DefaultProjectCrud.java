package acceler.ocdl.persistence.crud.impl;

import acceler.ocdl.model.Project;
import acceler.ocdl.persistence.crud.ProjectCrud;
import acceler.ocdl.persistence.dao.ProjectDao;

import java.util.Optional;

public class DefaultProjectCrud implements ProjectCrud{

    private ProjectDao projectDao;

    @Override
    public Project createProject(Project projectInfo) {
        projectInfo.setProjectId(null);
        return projectDao.save(projectInfo);
    }

    public Project updateProjct(Long id, Project updatedProjectInfo) {
        Optional<Project> projectOptional = projectDao.findById(id);
        if (projectOptional.isPresent()){
            updatedProjectInfo.setProjectId(id);
            return projectDao.save(projectOptional.get());
        }
        return null;
    }
}
