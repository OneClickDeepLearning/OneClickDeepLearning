package acceler.ocdl.service.impl;

import acceler.ocdl.dao.TemplateCategoryDao;
import acceler.ocdl.dao.TemplateDao;
import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.TemplateCategory;
import acceler.ocdl.exception.InvalidParamException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.service.HdfsService;
import acceler.ocdl.service.ProjectService;
import acceler.ocdl.service.TemplateService;
import acceler.ocdl.utils.TimeUtil;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import acceler.ocdl.CONSTANTS;
import acceler.ocdl.entity.Template;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Service
public class DBTemplateService implements TemplateService {

    @Autowired
    private TemplateCategoryDao templateCategoryDao;

    @Autowired
    private TemplateDao templateDao;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private HdfsService hdfsService;

    @Value("${HDFS.TEMPLATE}")
    private String hdfsTemplatePath;

    @Override
    @Transactional
    public Template uploadTemplate(Project project, String srcPath, TemplateCategory category) {

        File srcFile = new File(srcPath);
        if (!srcFile.isFile()) {
            throw new OcdlException(String.format("%s is not a file.", srcFile.getName()));
        }

        // upload file to HDFS
        String refId = CONSTANTS.TEMPLATE_TABLE.TEMPLATE_PREFIX
                + RandomStringUtils.randomAlphanumeric(CONSTANTS.PROJECT_DATA_TABLE.LENGTH_REF_ID);
        String desPath = Paths.get(hdfsTemplatePath, refId).toString();
        //hdfsService.uploadFile(new Path(srcPath), new Path(desPath));

        // create template in database
        Project projectInDb = projectService.getProject(project.getId());
        TemplateCategory templateCategoryInDb = templateCategoryDao.findById(category.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Fail to find category(#%s)", category.getId())));
        Template template = Template.builder()
                .name(srcFile.getName())
                .suffix(srcFile.getName().substring(srcFile.getName().lastIndexOf(".")+1))
                .refId(refId)
                .project(projectInDb)
                .templateCategory(templateCategoryInDb)
                .build();

        return createTemplate(template);

    }


    private Template createTemplate(Template template) {

        TemplateCategory templateCategory = templateCategoryDao.findById(template.getTemplateCategory().getId())
                .orElseThrow(() -> new NotFoundException(String.format("%s template category isn't exist.", template.getTemplateCategory().getId())));

        List<Template> data = templateDao.findByNameAndTemplateCategoryAndIsDeletedIsFalse(template.getName(), templateCategory);
        if (data.size() > 0) {
            throw new OcdlException(String.format("%s file is already exist.", template.getName()));
        }

        template.setCreatedAt(TimeUtil.currentTimeStampStr());
        template.setIsDeleted(false);

        return templateDao.save(template);
    }


    @Override
    public Page<Template> getTemplate(Template template, int page, int size) {

        Specification<Template> specification = new Specification<Template>() {

            @Override
            public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(template.getName())) {
                    predicates.add(criteriaBuilder.like(root.get(CONSTANTS.TEMPLATE_TABLE.NAME), "%" + template.getName() + "%"));
                }


                if (!StringUtils.isEmpty(template.getSuffix())) {
                    predicates.add(criteriaBuilder.like(root.get(CONSTANTS.TEMPLATE_TABLE.SUFFIX), "%" + template.getSuffix() + "%"));
                }

                if (template.getProject() != null) {
                    Project project = projectService.getProject(template.getId());
                    predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.TEMPLATE_TABLE.PROJECT), project));
                }

                if (template.getTemplateCategory() != null) {
                    TemplateCategory templateCategory = templateCategoryDao.findById(template.getTemplateCategory().getId())
                            .orElseThrow(() -> new NotFoundException(String.format("%s template category isn't exist.", template.getTemplateCategory().getId())));
                    predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.TEMPLATE_TABLE.CATEGORY), templateCategory));
                }

                if (template.getIsDeleted() == null) {
                    template.setIsDeleted(false);
                }
                predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.BASE_ENTITY.ISDELETED), template.getIsDeleted()));

                criteriaQuery.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        // sort and page
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC,"id"));
        PageRequest pageRequest = PageRequest.of(page, size,sort);

        return templateDao.findAll(specification, pageRequest);
    }


    @Override
    public boolean batchDeleteTemplate(List<Template> templates) {
        templates.forEach(
                each -> deleteTemplate(each.getId()));
        return true;
    }


    private boolean deleteTemplate(Long id) {

        Template templateInDb = templateDao.findByIdAndIsDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundException(String.format("Fail to find Template(# %d )", id)));

        templateInDb.setIsDeleted(true);
        templateInDb.setDeletedAt(TimeUtil.currentTimeStampStr());
        templateDao.save(templateInDb);
        return true;
    }


    @Override
    public TemplateCategory saveCategory(TemplateCategory category) {
        TemplateCategory categoryInDb = null;

        if (category.getId() != null) {
            categoryInDb = updateCategory(category);
        }else {
            categoryInDb = createCategory(category);
        }
        return categoryInDb;
    }

    private TemplateCategory updateCategory(TemplateCategory category) {

        TemplateCategory categoryInDb = templateCategoryDao.findById(category.getId())
                .orElseThrow(() ->
                        new NotFoundException(String.format("Fail to find TemplateCategory(# %d )", category.getId())));

        if (!StringUtils.isEmpty(category.getName())) {
            categoryInDb.setName(category.getName());
        }

        if (!StringUtils.isEmpty(category.getDescription())) {
            categoryInDb.setDescription(category.getDescription());
        }

       if (category.getParent() != null) {
           TemplateCategory parent = templateCategoryDao.findById(category.getParent().getId())
                   .orElseThrow(() -> new NotFoundException(String.format("%s parent category isn't exist.", category.getParent().getId())));
           categoryInDb.setParent(parent);
       }

       if (category.getShared() != null) {
           categoryInDb.setShared(category.getShared());
       }

        if (category.getIsDeleted() != null) {
            if (category.getIsDeleted() == true) {
                categoryInDb.setDeletedAt(TimeUtil.currentTimeStampStr());
            }
            categoryInDb.setIsDeleted(category.getIsDeleted());
        }

        return templateCategoryDao.save(categoryInDb);

    }


    private TemplateCategory createCategory(TemplateCategory category) {

        // check data validity
        // check name exist
        if (StringUtils.isEmpty(category.getName())) {
            throw new InvalidParamException("Category name shouldn't be empty.");
        }
        // check project exist
        Project project = projectService.getProject(category.getProject().getId());
        category.setProject(project);

        // check parent exist
        if (category.getParent() != null) {
            TemplateCategory parent = templateCategoryDao.findById(category.getParent().getId())
                    .orElseThrow(() -> new NotFoundException(String.format("%s parent category isn't exist.", category.getParent().getId())));
            category.setParent(parent);

            // check if category exist
            List<TemplateCategory> data = templateCategoryDao.findByNameAndParentAndIsDeletedIsFalse(category.getName(), parent);
            if (data.size() > 0) {
                throw new OcdlException(String.format("%s category is already exist.", category.getName()));
            }
        }

        // set default value
        if (category.getShared() == null) {
            category.setShared(false);
        }
        category.setCreatedAt(TimeUtil.currentTimeStampStr());
        category.setIsDeleted(false);

        // save
        return templateCategoryDao.save(category);
    }


    @Override
    public boolean deleteCategory(TemplateCategory category) {

        TemplateCategory categoryInDb = templateCategoryDao.findById(category.getId())
                .orElseThrow(() -> new NotFoundException(String.format("%s template category isn't exist.", category.getParent().getId())));

        if (categoryInDb.getTemplateList().size() > 0 || categoryInDb.getChildren().size() > 0) {
            throw new OcdlException("This category is not empty!");
        } else {
            categoryInDb.setIsDeleted(true);
            categoryInDb.setDeletedAt(TimeUtil.currentTimeStampStr());
            templateCategoryDao.save(categoryInDb);
        }
        return true;
    }

    @Override
    public List<TemplateCategory> getProjectCategory(Project project) {

        List<TemplateCategory> category = templateCategoryDao.findAllByProjectAndParent(project, null);

        return category;
    }

    @Override
    public boolean downloadTemplate(String refId, Project project) {

        Template templateInDb = templateDao.findByRefId(refId)
                .orElseThrow(() -> new NotFoundException(String.format("Fail to find template(#%s)", refId)));

        if (!templateInDb.getProject().getId().equals(project.getId())) {
            throw new OcdlException("Permission denied.");
        }

        String srcPath = Paths.get(hdfsTemplatePath, refId).toString();
        String desPath = Paths.get(CONSTANTS.APPLICATIONS_DIR.CONTAINER + templateInDb.getName()).toString();
        hdfsService.downloadFile(new Path(srcPath), new Path(desPath));
        return true;
    }

}
