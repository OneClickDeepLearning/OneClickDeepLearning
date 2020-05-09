package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.dao.UserDataDao;
import acceler.ocdl.entity.User;
import acceler.ocdl.entity.UserData;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.service.HdfsService;
import acceler.ocdl.service.UserDataService;
import acceler.ocdl.service.UserService;
import acceler.ocdl.utils.TimeUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DBUserDataService implements UserDataService {

    @Autowired
    private UserDataDao userDataDao;

    @Autowired
    private UserService userService;

    @Autowired
    private HdfsService hdfsService;

    @Value("${HDFS.USER_DATA}")
    private String hdfsUserDataPath;

    @Override
    @Transactional
    public UserData uploadUserData(String srcPath, User user) {

        File srcFile = new File(srcPath);
        if (!srcFile.isFile()) {
            throw new OcdlException(String.format("%s is not a file.", srcFile.getName()));
        }

        // upload file to HDFS
        String refId = CONSTANTS.USER_DATA_TABLE.USER_PREFIX + RandomStringUtils.randomAlphanumeric(CONSTANTS.PROJECT_DATA_TABLE.LENGTH_REF_ID);
        String desPath = Paths.get(hdfsUserDataPath, refId).toString();
        hdfsService.uploadFile(new Path(srcPath), new Path(desPath));

        // create userdata in database
        User userInDb = userService.getUserByUserId(user.getId());
        UserData userData = UserData.builder()
                .name(srcFile.getName())
                .suffix(srcFile.getName().substring(srcFile.getName().lastIndexOf(".")+1))
                .refId(refId)
                .user(userInDb)
                .build();
        return createUserData(userData);

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
                    User user = userService.getUserByUserId(userData.getUser().getId());
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

    @Override
    public boolean downloadUserData(String refId, User user) {

        UserData userData = userDataDao.findByRefId(refId)
                .orElseThrow(() -> new NotFoundException(String.format("Fail to find project data(#%s)", refId)));

        if (!userData.getUser().getId().equals(user.getId())) {
            throw new OcdlException("Permission denied.");
        }

        String srcPath = Paths.get(hdfsUserDataPath, refId).toString();
        String desPath = Paths.get(CONSTANTS.APPLICATIONS_DIR.CONTAINER + userData.getName()).toString();
        hdfsService.downloadFile(new Path(srcPath), new Path(desPath));
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
