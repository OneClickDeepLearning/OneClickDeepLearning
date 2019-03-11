package acceler.ocdl.persistence.crud.impl;

import acceler.ocdl.model.Project;
import acceler.ocdl.persistence.crud.ProjectCrud;
import acceler.ocdl.persistence.dao.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class DefaultProjectCrud implements ProjectCrud{

    @Autowired
    private ProjectDao projectDao;

    @Override
    public Project createProject(Project projectInfo) {
        projectInfo.setProjectId(null);
        return projectDao.save(projectInfo);
    }

    @Override
    public Project updateProjct(Long id, Project updatedProjectInfo) {
        Optional<Project> projectOptional = projectDao.findById(id);
        if (projectOptional.isPresent()){
            updatedProjectInfo.setProjectId(id);
            return projectDao.save(updatedProjectInfo);
        }
        return null;
    }

    @Override
    public Project updateProjectName(Long id, Project updatedProjectInfo) {
        Optional<Project> projectOptional = projectDao.findById(id);
        if (projectOptional.isPresent()){
            final Project po = projectOptional.get();
            if(updatedProjectInfo.getProjectName()!= null){
                po.setProjectName(updatedProjectInfo.getProjectName());
            }
            po.setProjectId(id);
            return projectDao.save(po);
        }
        return null;
    }

    @Override
    public Project fineById(Long id) {
        Optional<Project> projectOptional = projectDao.findById(id);
        if (projectOptional.isPresent()){
            return projectOptional.get();
        }
        return null;
    }
}
