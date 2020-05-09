package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.dao.AlgorithmDao;
import acceler.ocdl.dao.ModelDao;
import acceler.ocdl.dao.SuffixDao;
import acceler.ocdl.entity.*;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.service.*;
import acceler.ocdl.utils.TimeUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import org.apache.hadoop.fs.Path;


import java.nio.file.Paths;
import java.util.*;

@Service
public class DBModelService implements ModelService {

    private static final Logger log = Logger.getLogger(DBModelService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ModelDao modelDao;

    @Autowired
    private SuffixDao suffixDao;

    @Autowired
    private AlgorithmDao algorithmDao;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private HdfsService hdfsService;

    @Autowired
    private AlluxioService alluxioService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private MessageQueueService messageQueueService;

    @Value("${APPLICATIONS_DIR.USER_SPACE}")
    private String applicationsDirUserSpace;

    @Value("${HDFS.STAGING}")
    private String applicationsDirStageSpace;

    @Value("${S3.server.bucketName}")
    private String bucketName;

    @Override
    public Map<String, Integer> initModelToStage(User user, Project project) {

        user = userService.getUserByUserId(user.getId());
        project = projectService.getProject(project.getId());

        Map<String, Integer> initRecords = new HashMap<>();
        initRecords.put("finded", 0);
        initRecords.put("successUpload", 0);
        initRecords.put("failUpload", 0);

        // get the suffix list
        List<Suffix> suffixes = suffixDao.findAllByProject(project);

        if (suffixes.size() == 0) {
            throw new OcdlException("Please set project suffix first.");
        }

        final String userSpaceName = applicationsDirUserSpace + CONSTANTS.NAME_FORMAT.USER_SPACE.replace("{userId}", String.valueOf(user.getId()));
        log.info(userSpaceName);
        System.out.println(userSpaceName);
        final File userSpace = new File(userSpaceName);

        if (userSpace.isDirectory()) {

            File[] files = userSpace.listFiles();
            if (files == null) {
                log.error(String.format("fail in reading directory: %s", userSpaceName));
                throw new OcdlException(String.format("fail in reading directory: %s", userSpaceName));
            }

            int current = 0;
            for (; current < files.length; current++) {
                File f = files[current];

                if (!f.isDirectory() && isModelFile(suffixes, f.getName())) {
                    initRecords.put("finded", (int)initRecords.get("finded")+1);

                    try {

                        String refId = CONSTANTS.MODEL_TABLE.MODEL_PREFIX + RandomStringUtils.randomAlphanumeric(CONSTANTS.MODEL_TABLE.LENGTH_REF_ID);
                        String suffix = f.getName().substring(f.getName().lastIndexOf(".")+1);

                        String stagedFilePath = Paths.get(applicationsDirStageSpace,CONSTANTS.NAME_FORMAT.STAGED_MODEL.replace("{modelId}",
                                refId).replace("{suffix}", suffix)).toString();

                        String now = TimeUtil.currentTimeStampStr();

                        Model model = Model.builder()
                                .name(f.getName())
                                .suffix(suffix)
                                .status(ModelStatus.NEW)
                                .owner(user)
                                .lastOperator(user)
                                .refId(refId)
                                .project(project)
                                .createdAt(now)
                                .updatedAt(now)
                                .isDeleted(false)
                                .build();

                        if (modelDao.findAllByProjectAndName(project, f.getName()).size() > 0) {
                            throw new OcdlException("Model already exist, please change your name.");
                        }

                        hdfsService.uploadFile(new Path(f.getPath()), new Path(stagedFilePath));
                        modelDao.save(model);
                        initRecords.put("successUpload", (int)initRecords.get("successUpload")+1);

                    } catch (Exception e) {
                        e.printStackTrace();
                        initRecords.put("failUpload", (int) initRecords.get("failUpload") + 1);
                        log.error(String.format("Fail to init %s to new model", f.getName()));
                        continue;
                    }
                }
            }
        } else {
            throw new NotFoundException(String.format("%s, Fail to find user space.", user.getId()));
        }
        return initRecords;
    }

    @Override
    public void approveModel(Model model, String algorithmName, String comments, User lastOperator) {

        Model modelInDb = modelDao.findById(model.getId())
                .orElseThrow(() -> new OcdlException(String.format("%s Model name already exist.", model.getName())));

        Algorithm algorithm = algorithmDao.findByName(algorithmName)
                .orElseThrow(() -> new NotFoundException(String.format("%s algorithm is not found", algorithmName)));

        modelInDb.setStatus(ModelStatus.APPROVED);
        modelInDb.setComments(comments);
        modelInDb.setLastOperator(lastOperator);
        modelInDb.setAlgorithm(algorithm);
        modelInDb.setUpdatedAt(TimeUtil.currentTimeStampStr());

        modelDao.save(modelInDb);
    }

    @Override
    public void rejectModel(Model model, String comments, User lastOperator) {

        Model modelInDb = modelDao.findById(model.getId())
                .orElseThrow(() -> new OcdlException(String.format("%s Model name already exist.", model.getName())));

        modelInDb.setStatus(ModelStatus.REJECTED);
        modelInDb.setComments(comments);
        modelInDb.setLastOperator(lastOperator);
        modelInDb.setCachedVersion(0);
        modelInDb.setReleasedVersion(0);
        modelInDb.setAlgorithm(null);
        modelInDb.setUpdatedAt(TimeUtil.currentTimeStampStr());

        modelDao.save(modelInDb);
    }

    @Override
    public void undo(Model model, String comments, User lastOperator) {

        Model modelInDb = modelDao.findById(model.getId())
                .orElseThrow(() -> new OcdlException(String.format("%s Model name already exist.", model.getName())));

        modelInDb.setStatus(ModelStatus.NEW);
        modelInDb.setComments(comments);
        modelInDb.setLastOperator(lastOperator);
        modelInDb.setCachedVersion(0);
        modelInDb.setReleasedVersion(0);
        modelInDb.setAlgorithm(null);
        modelInDb.setUpdatedAt(TimeUtil.currentTimeStampStr());

        modelDao.save(modelInDb);

    }


    @Override
    public Model release(Model model, User user) {

        Algorithm algorithmInDb = algorithmDao.findById(model.getAlgorithm().getId())
                .orElseThrow(() -> new NotFoundException("Algorithm is not found."));

        String srcPath = Paths.get(applicationsDirStageSpace, model.getRefId() + "." + model.getSuffix()).toString();
        String desPath = Paths.get(applicationsDirUserSpace, model.getOwner().getId().toString(),model.getRefId() + "." + model.getSuffix()).toString();
        hdfsService.downloadFile(new Path(srcPath), new Path(desPath));
        File modelFile = new File(desPath);

        // upload file to AWS S3
        String publishedModelName = CONSTANTS.NAME_FORMAT.RELEASE_MODEL.replace("{algorithm}", algorithmInDb.getName())
                .replace("{release_version}", model.getReleasedVersion().toString())
                .replace("{cached_version}", model.getCachedVersion().toString())
                .replace("{suffix}", model.getSuffix());
        storageService.uploadObject(bucketName, publishedModelName, modelFile);

        //send kafka message
        String message = CONSTANTS.KAFKA.MESSAGE.replace("{publishedModelName}", publishedModelName).replace("{modelUrl}",storageService.getObkectUrl(bucketName, publishedModelName));
        System.out.println("+++++++++++++++++++++++++++++++++++++++");
        System.out.println(message);
        if (StringUtils.isEmpty(algorithmInDb.getKafkaTopic())) {
            throw new OcdlException("Please set the Topic in Algorithm first.");
        }
        messageQueueService.send(algorithmInDb.getKafkaTopic(), message);

        algorithmInDb.setCurrentCachedVersion(model.getCachedVersion());
        algorithmInDb.setCurrentReleasedVersion(model.getReleasedVersion());

        model.setAlgorithm(algorithmInDb);
        model.setIsReleased(true);
        model.setUpdatedAt(TimeUtil.currentTimeStampStr());
        model.setLastOperator(userService.getUserByUserId(user.getId()));
        return modelDao.save(model);
    }
    
    @Override
    public Model createModel(Model model) {
        
        if (modelDao.findAllByProjectAndName(model.getProject(), model.getName()).size() > 0) {
            throw new OcdlException(String.format("%s Model name already exist.", model.getName()));
        }

        model.setOwner(userService.getUserByUserId(model.getOwner().getId()));
        model.setLastOperator(userService.getUserByUserId(model.getLastOperator().getId()));
        model.setProject(projectService.getProject(model.getProject().getId()));
        model.setCreatedAt(TimeUtil.currentTimeStampStr());
        model.setIsDeleted(false);
        
        return modelDao.save(model);
    }
    

    @Override
    public Model updateModel(Model model) {
        
        String current = TimeUtil.currentTimeStampStr();
        
        Model modelInDb = modelDao.findById(model.getId())
                .orElseThrow(() -> new NotFoundException(String.format("%s file is already exist.", model.getId())));

        if (!StringUtils.isEmpty(model.getName())) {
            modelInDb.setName(model.getName());
        }
        
        if (!StringUtils.isEmpty(model.getSuffix())) {
            modelInDb.setSuffix(model.getSuffix());
        }
        
        if (model.getLastOperator() != null) {
            User user = userService.getUserByUserId(model.getLastOperator().getId());
            modelInDb.setLastOperator(user);
        }
        
        if (!StringUtils.isEmpty(model.getComments())) {
            modelInDb.setComments(model.getComments());
        }
        
        if (model.getAlgorithm() != null) {
            Algorithm algorithm = algorithmDao.findById(model.getAlgorithm().getId())
                    .orElseThrow(() -> new NotFoundException(String.format("Fail to find algorithm(#%s)", model.getAlgorithm().getId())));
            modelInDb.setAlgorithm(algorithm);

            if (modelInDb.getStatus().equals(ModelStatus.NEW) && model.getStatus().equals(ModelStatus.APPROVED)) {
                if (model.getIsCachedVersion()) {
                    modelInDb.setReleasedVersion(algorithm.getCurrentReleasedVersion());
                    modelInDb.setCachedVersion(algorithm.getCurrentCachedVersion()+1);
                } else {
                    modelInDb.setReleasedVersion(algorithm.getCurrentReleasedVersion()+1);
                    modelInDb.setCachedVersion(0);
                }
            }
        }
        
        if (model.getIsDeleted() != null) {
            if (model.getIsDeleted() == true) {
                modelInDb.setDeletedAt(current);
            }
            modelInDb.setIsDeleted(model.getIsDeleted());
        }

        if (model.getStatus() != null) {
            modelInDb.setStatus(model.getStatus());
        }
        
        modelInDb.setUpdatedAt(current);

        return modelDao.save(modelInDb);
    }

    @Override
    public Boolean deleteModel(Model model) {
        
        Model modelInDb = modelDao.findById(model.getId())
                .orElseThrow(() -> new NotFoundException(String.format("%s model is not found.", model.getId())));
        
        modelInDb.setIsDeleted(true);
        modelInDb.setDeletedAt(TimeUtil.currentTimeStampStr());
        
        modelDao.save(modelInDb);
        return true;
    }

    @Override
    public Page<Model> getModels(Model model, int page, int size) {
        
        Specification<Model> specification = new Specification<Model>() {

            @Override
            public Predicate toPredicate(Root<Model> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(model.getName())) {
                    predicates.add(criteriaBuilder.like(root.get(CONSTANTS.MODEL_TABLE.NAME), "%" + model.getName() + "%"));
                }


                if (!StringUtils.isEmpty(model.getSuffix())) {
                    predicates.add(criteriaBuilder.like(root.get(CONSTANTS.MODEL_TABLE.SUFFIX), "%" + model.getSuffix() + "%"));
                }

                if (model.getProject() != null) {
                    Project project = projectService.getProject(model.getProject().getId());
                    predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.MODEL_TABLE.PROJECT), project));
                }

                if (model.getOwner() != null) {
                    predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.MODEL_TABLE.OWNER), model.getOwner()));
                }

                if (model.getStatus() != null) {
                    predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.MODEL_TABLE.STATUS), model.getStatus()));
                }

                if (model.getIsDeleted() == null) {
                    model.setIsDeleted(false);
                }
                predicates.add(criteriaBuilder.equal(root.get(CONSTANTS.BASE_ENTITY.ISDELETED), model.getIsDeleted()));

                criteriaQuery.distinct(true);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        // sort and page
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC,"id"));
        PageRequest pageRequest = PageRequest.of(page, size,sort);

        return modelDao.findAll(specification, pageRequest);
    }

    private boolean isModelFile(List<Suffix> modelSuffixes, String fileName) {
        return modelSuffixes.stream().anyMatch(s -> fileName.trim().endsWith(s.getName()));
    }

    @Override
    public Model getModelById(Long id) {
        return modelDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Fail to find model(#%s)", id)));
    }

}
