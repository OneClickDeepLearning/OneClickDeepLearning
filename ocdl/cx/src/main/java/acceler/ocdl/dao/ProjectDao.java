package acceler.ocdl.dao;

import acceler.ocdl.model.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectDao extends CrudRepository<Long, Project>{
    Project findByprojectName(String projectName);
}
