package acceler.ocdl.persistence.impl;

import acceler.ocdl.model.Project;
import acceler.ocdl.persistence.ProjectCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DefaultProjectCrud implements ProjectCrud{

    @Autowired
    private Persistence persistence;

    @Override
    public Project updateProject(Project updatedProjectInfo) {
        Project project = persistence.getProject();
        project.setProjectName(updatedProjectInfo.getProjectName());
        project.setGitRepoURI(updatedProjectInfo.getGitRepoURI());
        project.setTemplatePath(updatedProjectInfo.getTemplatePath());
        project.setK8MasterUri(updatedProjectInfo.getK8MasterUri());
        project.setSuffix(updatedProjectInfo.getSuffix());

        persistence.persistentProject();
        return project;
    }

    //FIXME: refectory: why provide updateProjectName?
    @Override
    public Project updateProjectName(String name) {
        Project project = persistence.getProject();
        project.setProjectName(name);

        persistence.persistentProject();
        return project;
    }

    @Override
    public Project getProjectConfiguration() {
        return persistence.getProject();
    }

    @Override
    public String getProjectName() { return persistence.getProject().getProjectName();}
}
