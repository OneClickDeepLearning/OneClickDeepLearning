package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.dao.UserDataDao;
import acceler.ocdl.entity.User;
import acceler.ocdl.entity.UserData;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.service.UserDataService;
import acceler.ocdl.service.UserService;
import acceler.ocdl.utils.TimeUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class DBUserDataService implements UserDataService {

    @Autowired
    private UserDataDao userDataDao;

    @Autowired
    private UserService userService;


    @Override
    @Transactional
    public UserData uploadUserData() {

        // TODO: upload file to HDFS
        String refId = RandomStringUtils.randomAlphanumeric(CONSTANTS.PROJECT_DATA_TABLE.LENGTH_REF_ID);

        // create userData in database
        return null;

    }


    private UserData createUserData(UserData userData) {

        List<UserData> data = userDataDao.findByNameAndIsDeletedIsFalse(userData.getName());
        if (data.size() > 0) {
            throw new OcdlException(String.format("%s file is already exist.", userData.getName()));
        }

        userData.setCreatedAt(TimeUtil.currentTimeStampStr());
        userData.setIsDeleted(false);

        return userDataDao.save(userData);
    }


    @Override
    public Page<UserData> getUserData(UserData userData, int page, int size) {

        Specification<UserData> specification = new Specification<UserData>() {

            @Override
            public Predicate toPredicate(Root<UserData> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(userData.getName())) {
                    predicates.add(criteriaBuilder.like(root.get(CONSTANTS.USER_DATA_TABLE.NAME), "%" + userData.getName() + "%"));
                }


                if (!StringUtils.isEmpty(userData.getSuffix())) {
                    predicates.add(criteriaBuilder.like(root.get(CONSTANTS.USER_DATA_TABLE.SUFFIX), "%" + userData.getSuffix() + "%"));
                }

                if (userData.getUser() != null) {
                    User user = userService.getUserByUserId(userData.getId());
                    predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.USER_DATA_TABLE.USER), user));
                }

                if (userData.getIsDeleted() == null) {
                    userData.setIsDeleted(false);
                }
                predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.BASE_ENTITY.ISDELETED), userData.getIsDeleted()));

                criteriaQuery.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        // sort and page
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC,"id"));
        PageRequest pageRequest = PageRequest.of(page, size,sort);

        return userDataDao.findAll(specification, pageRequest);
    }


    @Override
    public boolean batchDeleteUserData(List<UserData> userDatas) {
        userDatas.forEach(
                each -> {
                    deleteUserData(each.getId());
                }
        );
        return true;
    }


    private boolean deleteUserData(Long id) {

        UserData userDataInDb = userDataDao.findByIdAndIsDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundException(String.format("Fail to find UserData(# %d )", id)));

        userDataInDb.setIsDeleted(true);
        userDataInDb.setDeletedAt(TimeUtil.currentTimeStampStr());
        userDataDao.save(userDataInDb);
        return true;
    }
}
