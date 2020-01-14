package acceler.ocdl.service.impl;


import acceler.ocdl.dao.AlgorithmDao;
import acceler.ocdl.dao.ProjectDao;
import acceler.ocdl.entity.Project;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.service.ProjectService;
import acceler.ocdl.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class DBProjectService implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Override
    public Project saveProject(Project project) {

        Project projectInDb = null;

        if (project.getId() != null) {
            projectInDb = updateProject(project);
        } else {
            projectInDb = createProject(project);
        }

        return projectInDb;
    }


    private Project createProject(Project project) {

        project.setCreatedAt(TimeUtil.currentTimeStampStr());
        project.setIsDeleted(false);

        return projectDao.save(project);
    }


    private Project updateProject(Project updatedProject) {

        Project projectInDb = projectDao.findById(updatedProject.getId())
                .orElseThrow(() ->
                        new NotFoundException(String.format("Fail to find Project(# %d )", updatedProject.getId())));

        if (!StringUtils.isEmpty(updatedProject.getName())) {
            projectInDb.setName(updatedProject.getName());
        }

        if (!StringUtils.isEmpty(updatedProject.getDescription())) {
            projectInDb.setDescription(updatedProject.getDescription());
        }

        if (updatedProject.getIsDeleted() != null) {
            if (updatedProject.getIsDeleted() == true) {
                projectInDb.setDeletedAt(TimeUtil.currentTimeStampStr());
            }
            projectInDb.setIsDeleted(updatedProject.getIsDeleted());
        }

        return projectDao.save(projectInDb);
    }

    @Override
    public Project getProject(Long id) {

        return projectDao.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundException(String.format("Fail to find Project(# %d )", id)));
    }


    @Override
    public boolean deleteProject(Long id) {

        Project projectInDb = projectDao.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundException(String.format("Fail to find Project(# %d )", id)));

        projectInDb.setIsDeleted(false);
        projectInDb.setDeletedAt(TimeUtil.currentTimeStampStr());
        projectDao.save(projectInDb);
        return true;
    }
}
