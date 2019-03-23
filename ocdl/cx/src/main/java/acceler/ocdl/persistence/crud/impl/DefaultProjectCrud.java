package acceler.ocdl.persistence.crud.impl;

import acceler.ocdl.model.Project;
import acceler.ocdl.persistence.crud.ProjectCrud;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class DefaultProjectCrud implements ProjectCrud{


    @Override
    public Project updateProjct(Project updatedProjectInfo) {
        return null;
    }

    @Override
    public Project updateProjectName(String name) {
        return null;
    }

    @Override
    public Project getProjectConfiguration() {
        return null;
    }
}
