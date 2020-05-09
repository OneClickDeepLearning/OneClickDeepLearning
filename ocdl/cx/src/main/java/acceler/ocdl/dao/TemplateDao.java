package acceler.ocdl.dao;

import acceler.ocdl.entity.Template;
import acceler.ocdl.entity.TemplateCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface TemplateDao extends JpaRepository<Template, Long>, JpaSpecificationExecutor<Template> {

    Optional<Template> findByRefId(String refId);

    List<Template> findByNameAndTemplateCategoryAndIsDeletedIsFalse(String name, TemplateCategory templateCategory);

    Optional<Template> findByIdAndIsDeletedIsFalse(Long id);
}
