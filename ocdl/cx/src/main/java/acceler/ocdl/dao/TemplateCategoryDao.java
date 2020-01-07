package acceler.ocdl.dao;

import acceler.ocdl.entity.TemplateCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TemplateCategoryDao extends JpaRepository<TemplateCategory, Long>, JpaSpecificationExecutor<TemplateCategory> {

    Optional<TemplateCategory> findByName(String name);
}