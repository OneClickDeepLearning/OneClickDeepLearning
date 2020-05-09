package acceler.ocdl.dao;

import acceler.ocdl.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface ProjectDao extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    Optional<Project> findByName(String name);

    Optional<Project> findByIdAndIsDeletedIsFalse(Long id);

    Optional<Project> findByRefId(String refId);
}
