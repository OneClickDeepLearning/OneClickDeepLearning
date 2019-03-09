package acceler.ocdl.persistence.dao;

import acceler.ocdl.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDao extends CrudRepository<Project, Long>{
    Project findByprojectName(String projectName);
}
