package acceler.ocdl.dao;

import acceler.ocdl.entity.TemplateCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TemplateCategoryDao extends JpaRepository<TemplateCategory, Long>, JpaSpecificationExecutor<TemplateCategory> {
}
