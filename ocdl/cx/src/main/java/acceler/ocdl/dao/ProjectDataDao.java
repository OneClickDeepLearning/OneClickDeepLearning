package acceler.ocdl.dao;

import acceler.ocdl.entity.ProjectData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProjectDataDao extends JpaRepository<ProjectData, Long>, JpaSpecificationExecutor<ProjectData> {

    Optional<ProjectData> findByRefId(String refId);

}
