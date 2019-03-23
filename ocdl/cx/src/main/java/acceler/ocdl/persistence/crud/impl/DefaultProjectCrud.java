package acceler.ocdl.persistence.crud.impl;

import acceler.ocdl.model.Project;
import acceler.ocdl.persistence.crud.ProjectCrud;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class DefaultProjectCrud implements ProjectCrud{

    @Override
    public Project createProject(Project projectInfo) {
        return null;
    }

    @Override
    public Project updateProjct(Long id, Project updatedProjectInfo) {
        return null;
    }

    @Override
    public Project updateProjectName(Long id, Project updatedProjectInfo) {

        return null;
    }

    @Override
    public Project fineById(Long id) {
        return null;
    }

}
