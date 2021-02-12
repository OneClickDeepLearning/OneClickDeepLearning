package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.dao.SuffixDao;
import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.Suffix;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.service.ProjectService;
import acceler.ocdl.service.SuffixService;
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
public class DBSuffixService implements SuffixService {

    @Autowired
    private SuffixDao suffixDao;

    @Autowired
    private ProjectService projectService;

    @Override
    public Suffix createSuffix(Suffix suffix) {

        suffix.setCreatedAt(TimeUtil.currentTimeStampStr());
        suffix.setIsDeleted(false);

        return suffixDao.save(suffix);
    }

    @Override
    public Page<Suffix> getSuffix(Suffix suffix, int page, int size) {

        Specification<Suffix> specification = new Specification<Suffix>(){

            @Override
            public Predicate toPredicate(Root<Suffix> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(suffix.getName())) {
                    predicates.add(criteriaBuilder.like(root.get(CONSTANTS.SUFFIX_TABLE.NAME), "%" + suffix.getName() + "%"));
                }

                if (suffix.getProject  () != null) {
                    Project project = projectService.getProject(suffix.getProject().getId());
                    predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.SUFFIX_TABLE.PROJECT), project));
                }

                if (suffix.getIsDeleted() == null) {
                    suffix.setIsDeleted(false);
                }
                predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.BASE_ENTITY.ISDELETED), suffix.getIsDeleted()));

                criteriaQuery.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        // sort and page
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC,"id"));
        PageRequest pageRequest = PageRequest.of(page, size,sort);

        return suffixDao.findAll(specification, pageRequest);
    }


    @Override
    public boolean batchDeleteSuffix(List<Suffix> suffixs) {
        suffixs.forEach(
                al -> {
                    deleteSuffix(al.getId());
                }
        );
        return true;
    }

    public boolean deleteSuffix(Long id) {

        Suffix suffixInDb = suffixDao.findByIdAndIsDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundException(String.format("Fail to find Suffix(# %d )", id)));

        suffixInDb.setIsDeleted(true);
        suffixInDb.setDeletedAt(TimeUtil.currentTimeStampStr());
        suffixDao.save(suffixInDb);
        return true;
    }
}
