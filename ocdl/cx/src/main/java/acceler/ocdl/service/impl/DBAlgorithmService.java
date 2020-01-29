package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.dao.AlgorithmDao;
import acceler.ocdl.entity.Algorithm;
import acceler.ocdl.entity.Project;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.service.AlgorithmService;
import acceler.ocdl.service.ProjectService;
import acceler.ocdl.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class DBAlgorithmService implements AlgorithmService {
    

    @Autowired
    private AlgorithmDao algorithmDao;

    @Autowired
    private ProjectService projectService;

    @Override
    public Algorithm saveAlgorithm(Algorithm algorithm) {

        Algorithm algorithmInDb = null;

        if (algorithm.getId() != null) {
            algorithmInDb = updateAlgorithm(algorithm);
        }else {
            algorithmInDb = createAlgorithm(algorithm);
        }
        return algorithmInDb;
    }


    private Algorithm createAlgorithm(Algorithm algorithm) {

        if (algorithm.getProject() == null) {
            throw new OcdlException("Project couldn't be null.");
        }

        Project project = projectService.getProject(algorithm.getProject().getId());
        algorithmDao.findByNameAndProject(algorithm.getName(), project).ifPresent(al -> {
            throw new OcdlException(String.format("Algorithm %s already exist.", al.getName()));
        });

        algorithm.setCreatedAt(TimeUtil.currentTimeStampStr());
        algorithm.setIsDeleted(false);
        algorithm.setCurrentCachedVersion(0);
        algorithm.setCurrentReleasedVersion(0);

        return algorithmDao.save(algorithm);
    }


    private Algorithm updateAlgorithm(Algorithm updatedAlgorithm) {

        Algorithm algorithmInDb = algorithmDao.findById(updatedAlgorithm.getId())
                .orElseThrow(() ->
                        new NotFoundException(String.format("Fail to find Algorithm(# %d )", updatedAlgorithm.getId())));

        if (!StringUtils.isEmpty(updatedAlgorithm.getName())) {
            algorithmInDb.setName(updatedAlgorithm.getName());
        }

        if (!StringUtils.isEmpty(updatedAlgorithm.getDescription())) {
            algorithmInDb.setDescription(updatedAlgorithm.getDescription());
        }

        if (updatedAlgorithm.getCurrentCachedVersion() != null) {
            algorithmInDb.setCurrentCachedVersion(updatedAlgorithm.getCurrentCachedVersion());
        }

        if (updatedAlgorithm.getCurrentReleasedVersion() != null) {
            algorithmInDb.setCurrentReleasedVersion(updatedAlgorithm.getCurrentReleasedVersion());
        }

        if (updatedAlgorithm.getIsDeleted() != null) {
            if (updatedAlgorithm.getIsDeleted() == true) {
                algorithmInDb.setDeletedAt(TimeUtil.currentTimeStampStr());
            }
            algorithmInDb.setIsDeleted(updatedAlgorithm.getIsDeleted());
        }

        return algorithmDao.save(algorithmInDb);
    }

    @Override
    public Page<Algorithm> getAlgorithm(Algorithm algorithm, int page, int size) {

        Specification<Algorithm> specification = new Specification<Algorithm>(){

            @Override
            public Predicate toPredicate(Root<Algorithm> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(algorithm.getName())) {
                    predicates.add(criteriaBuilder.like(root.get(CONSTANTS.ALGORITHM_TABLE.NAME), "%" + algorithm.getName() + "%"));
                }


                if (!StringUtils.isEmpty(algorithm.getDescription())) {
                    predicates.add(criteriaBuilder.like(root.get(CONSTANTS.ALGORITHM_TABLE.DESCRIPTION), "%" + algorithm.getDescription() + "%"));
                }

                if (algorithm.getProject() != null) {
                    Project project = projectService.getProject(algorithm.getProject().getId());
                    predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.ALGORITHM_TABLE.PROJECT), project));
                }

                if (algorithm.getIsDeleted() == null) {
                    algorithm.setIsDeleted(false);
                }
                predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.BASE_ENTITY.ISDELETED), algorithm.getIsDeleted()));

                criteriaQuery.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        // sort and page
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC,"id"));
        PageRequest pageRequest = PageRequest.of(page, size,sort);

        return algorithmDao.findAll(specification, pageRequest);
    }


    @Override
    public boolean batchDeleteAlgorithm(List<Algorithm> algorithms) {
        algorithms.forEach(
                al -> {
                    deleteAlgorithm(al.getId());
                }
        );
        return true;
    }


    public boolean deleteAlgorithm(Long id) {

        Algorithm algorithmInDb = algorithmDao.findByIdAndIsDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundException(String.format("Fail to find Algorithm(# %d )", id)));

        algorithmInDb.setIsDeleted(true);
        algorithmInDb.setDeletedAt(TimeUtil.currentTimeStampStr());
        algorithmDao.save(algorithmInDb);
        return true;
    }
}
