package acceler.ocdl.service;

import acceler.ocdl.entity.Project;

public interface ProjectService {

    /**
     * create/update project configurations
     * if Param project has id, then update
     * else create new project record
     * @param project project
     * @return project in updated database
     */
    Project saveProject(Project project);

    /**
     * get project according project id
     * @param id project id
     * @return Project in database
     */
    Project getProject(Long id);

    /**
     * delete Project obj
     * @param id project id
     * @return if delete success
     */
    boolean deleteProject(Long id);
}
