package acceler.ocdl.dao;

import acceler.ocdl.entity.Model;
import acceler.ocdl.entity.ModelStatus;
import acceler.ocdl.entity.Project;
import com.sun.tools.doclint.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ModelDao extends JpaRepository<Model, Long>, JpaSpecificationExecutor<Model> {

    Optional<Model> findByRefId(String refId);

    List<Model> findAllByProjectAndName(Project project, String name);

    Optional<Model> findByIdAndStatus(Long id, ModelStatus approved);
}
