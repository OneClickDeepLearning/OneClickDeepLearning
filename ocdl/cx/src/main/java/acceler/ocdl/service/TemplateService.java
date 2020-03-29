package acceler.ocdl.service;

import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.Template;
import acceler.ocdl.entity.TemplateCategory;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface TemplateService {

//    Map<String,List<String>> getTemplatesList();
//
//    List<String> getCode(String name,String type);

    @Transactional
    Template uploadTemplate(Project project, String srcPath, TemplateCategory category);

    Page<Template> getTemplate(Template template, int page, int size);

    boolean batchDeleteTemplate(List<Template> templates);

    TemplateCategory saveCategory(TemplateCategory category);

    boolean deleteCategory(TemplateCategory category);

    List<TemplateCategory> getProjectCategory(Project project);

    boolean downloadTemplate(String refId, Project project);

}
