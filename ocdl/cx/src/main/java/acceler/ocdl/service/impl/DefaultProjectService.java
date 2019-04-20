package acceler.ocdl.service.impl;

import acceler.ocdl.model.Project;
import acceler.ocdl.persistence.ProjectCrud;
import acceler.ocdl.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DefaultProjectService implements ProjectService {


    @Autowired
    private ProjectCrud projectCrud;

    @Override
    public String RequestProjectName() {
        Project project = projectCrud.getProjectConfiguration();
        return project.getProjectName();
    }

    @Override
    public Project RequestAllConfigurationInfo() {
        return projectCrud.getProjectConfiguration();
    }

    @Override
    public void updateProject(Project project) {
        projectCrud.updateProject(project);
    }
    public void updateProjectName(String name){
        projectCrud.updateProjectName(name);
    }
}
