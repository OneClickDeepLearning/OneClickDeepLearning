package acceler.ocdl.dao;

import acceler.ocdl.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TemplateDao extends JpaRepository<Template, Long>, JpaSpecificationExecutor<Template> {
}