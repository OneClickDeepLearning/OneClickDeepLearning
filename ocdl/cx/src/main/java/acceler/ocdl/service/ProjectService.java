package acceler.ocdl.service;

import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.User;

public interface ProjectService {

    /**
     * create/update project configurations
     * if Param project has id, then update
     * else create new project record
     * @param project project
     * @return project in updated database
     */
    Project saveProject(Project project, User user);

    /**
     * get project according project id
     * @param id project id
     * @return Project in database
     */
    Project getProject(Long id);

    Project getProject(String refId);

    /**
     * delete Project obj
     * @param id project id
     * @return if delete success
     */
    boolean deleteProject(Long id);
}
