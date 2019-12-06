package acceler.ocdl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface Project extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
}
