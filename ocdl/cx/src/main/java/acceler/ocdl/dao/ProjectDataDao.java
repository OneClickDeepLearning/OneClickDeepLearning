package acceler.ocdl.dao;

import acceler.ocdl.entity.ProjectData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectDataDao extends JpaRepository<ProjectData, Long>, JpaSpecificationExecutor<ProjectData> {
}
