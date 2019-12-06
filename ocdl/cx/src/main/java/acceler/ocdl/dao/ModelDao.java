package acceler.ocdl.dao;

import acceler.ocdl.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ModelDao extends JpaRepository<Model, Long>, JpaSpecificationExecutor<Model> {
}
