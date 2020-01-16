package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.dao.ProjectDataDao;
import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.ProjectData;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.service.ProjectDataService;
import acceler.ocdl.service.ProjectService;
import acceler.ocdl.utils.TimeUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class DBProjectDataService implements ProjectDataService {

    @Autowired
    private ProjectDataDao projectDataDao;

    @Autowired
    private ProjectService projectService;


    @Override
    @Transactional
    public ProjectData uploadProjectData() {

        // TODO: upload file to HDFS
        String refId = CONSTANTS.PROJECT_DATA_TABLE.PROJECT_PREFIX + RandomStringUtils.randomAlphanumeric(CONSTANTS.PROJECT_DATA_TABLE.LENGTH_REF_ID);

        // create projectData in database
        return null;

    }


    private ProjectData createProjectData(ProjectData projectData) {

        List<ProjectData> data = projectDataDao.findByNameAndIsDeletedIsFalse(projectData.getName());
        if (data.size() > 0) {
            throw new OcdlException(String.format("%s file is already exist.", projectData.getName()));
        }

        projectData.setCreatedAt(TimeUtil.currentTimeStampStr());
        projectData.setIsDeleted(false);

        return projectDataDao.save(projectData);
    }


    @Override
    public Page<ProjectData> getProjectData(ProjectData projectData, int page, int size) {

        Specification<ProjectData> specification = new Specification<ProjectData>() {

            @Override
            public Predicate toPredicate(Root<ProjectData> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(projectData.getName())) {
                    predicates.add(criteriaBuilder.like(root.get(CONSTANTS.PROJECT_DATA_TABLE.NAME), "%" + projectData.getName() + "%"));
                }


                if (!StringUtils.isEmpty(projectData.getSuffix())) {
                    predicates.add(criteriaBuilder.like(root.get(CONSTANTS.PROJECT_DATA_TABLE.SUFFIX), "%" + projectData.getSuffix() + "%"));
                }

                if (projectData.getProject() != null) {
                    Project project = projectService.getProject(projectData.getId());
                    predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.PROJECT_DATA_TABLE.PROJECT), project));
                }

                if (projectData.getIsDeleted() == null) {
                    projectData.setIsDeleted(false);
                }
                predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.BASE_ENTITY.ISDELETED), projectData.getIsDeleted()));

                criteriaQuery.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        // sort and page
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC,"id"));
        PageRequest pageRequest = PageRequest.of(page, size,sort);

        return projectDataDao.findAll(specification, pageRequest);
    }


    @Override
    public boolean batchDeleteProjectData(List<ProjectData> projectDatas) {
        projectDatas.forEach(
                each -> {
                    deleteProjectData(each.getId());
                }
        );
        return true;
    }


    private boolean deleteProjectData(Long id) {

        ProjectData projectDataInDb = projectDataDao.findByIdAndIsDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundException(String.format("Fail to find ProjectData(# %d )", id)));

        projectDataInDb.setIsDeleted(true);
        projectDataInDb.setDeletedAt(TimeUtil.currentTimeStampStr());
        projectDataDao.save(projectDataInDb);
        return true;
    }
}
