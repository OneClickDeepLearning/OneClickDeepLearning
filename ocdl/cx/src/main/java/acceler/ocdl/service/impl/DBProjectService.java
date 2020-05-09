package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.dao.ProjectDao;
import acceler.ocdl.dao.RoleDao;
import acceler.ocdl.entity.*;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.service.ProjectService;
import acceler.ocdl.service.TemplateService;
import acceler.ocdl.service.UserService;
import acceler.ocdl.utils.TimeUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DBProjectService implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private TemplateService templateService;

    @Override
    @Transactional
    public Project saveProject(Project project, User user) {

        Project projectInDb = null;

        if (project.getId() != null) {
            if (getProject(project.getId())
                    .getUserRoles().stream()
                    .anyMatch(rUserRole -> rUserRole.getUser().getId().equals(user.getId()))) {
                throw new OcdlException("Permission denied.");
            }
            projectInDb = updateProject(project);
        } else {
            projectInDb = createProject(project);

            // connect user and project
            Role role = roleDao.findByName(CONSTANTS.ROLE_TABLE.ROLE_MAN)
                    .orElseThrow(() ->
                            new NotFoundException(String.format("Fail to find role(#%s)", CONSTANTS.ROLE_TABLE.ROLE_MAN)));
            userService.addRoleRelation(user, role, projectInDb);

            // create root template category to project
            TemplateCategory templateCategory = TemplateCategory.builder()
                    .name("root")
                    .project(projectInDb)
                    .build();
            templateService.saveCategory(templateCategory);
        }

        return projectDao.findById(projectInDb.getId()).get();
    }


    private Project createProject(Project project) {

        if (projectDao.findByName(project.getName()).isPresent()) {
            throw new OcdlException("Project already exist.");
        }

        // create project in Db
        project.setRefId(RandomStringUtils.randomAlphanumeric(CONSTANTS.PROJECT_TABLE.LENGTH_REF_ID));
        project.setCreatedAt(TimeUtil.currentTimeStampStr());
        project.setIsDeleted(false);
        Project projectInDb = projectDao.save(project);

        return projectInDb;
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

        return projectDao.findByIdAndIsDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundException(String.format("Fail to find Project(# %d )", id)));
    }


    @Override
    public Project getProject(String refId) {

        return projectDao.findByRefId(refId)
                .orElse(null);
    }


    @Override
    @Transactional
    public boolean deleteProject(Long id) {

        Project projectInDb = projectDao.findByIdAndIsDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundException(String.format("Fail to find Project(# %d )", id)));

        projectInDb.setIsDeleted(true);
        projectInDb.setDeletedAt(TimeUtil.currentTimeStampStr());
        projectDao.save(projectInDb);

        projectInDb.getUserRoles().forEach(rUserRole -> userService.deleteRoleRelation(rUserRole.getId()));
        return true;
    }


}
