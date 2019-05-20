package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.model.*;
import acceler.ocdl.service.ModelService;
import acceler.ocdl.utils.CommandHelper;
import acceler.ocdl.utils.TimeUtil;
import io.fabric8.kubernetes.api.model.AffinityFluent;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Service
@DependsOn({"storageLoader"})
public class DefaultModelServiceImpl implements ModelService {

    private static final Logger log = Logger.getLogger(DefaultModelServiceImpl.class);

    @Autowired
    private CommandHelper commandHelper;

    @Override
    public void initModelToStage(InnerUser innerUser) {

        final String userSpaceName = CONSTANTS.APPLICATIONS_DIR.USER_SPACE + CONSTANTS.NAME_FORMAT.USER_SPACE.replace("{projectName}", Project.getProjectNameInStorage()).replace("{userId}", String.valueOf(innerUser.getUserId()));
        final File userSpace = new File(userSpaceName);

        if (userSpace.isDirectory()) {
            File[] files = userSpace.listFiles();
            List<String> suffixes = Arrays.asList(Project.getModelFileSuffixesInStorage());

            //IO error occures
            if (files == null) {
                log.error(String.format("fail in reading directory: %s", userSpaceName));
                throw new OcdlException(String.format("fail in reading directory: %s", userSpaceName));
            }
            int current = 0;
            try {
                for (; current < files.length; current++) {
                    File f = files[current];

                    if (!f.isDirectory() && isModelFile(suffixes, f.getName())) {
                        Long modelId = Model.generateModelId();

                        String suffix = f.getName().substring(f.getName().lastIndexOf(".")+1);

                        String stagedFilePath = CONSTANTS.APPLICATIONS_DIR.STAGE_SPACE +
                                CONSTANTS.NAME_FORMAT.STAGED_MODEL.replace("{modelId}", modelId.toString()).replace("{suffix}", suffix);

                        File newStagedFilePath = new File(stagedFilePath);
                        FileUtils.moveFile(f, newStagedFilePath);
                        persistNewModel(f, modelId);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error(String.format("fail to create new model, because %s failed to move to stage space", files[current].getName()));
                throw new OcdlException(String.format("fail to create new model, because %s failed to move to stage space", files[current].getName()));
            }
        } else {
            throw new NotFoundException(String.format("%s, Fail to find user space.", innerUser.getUserId()));
        }
    }

    private void persistNewModel(File modelFile, Long modelId) {
        NewModel model = new NewModel();
        model.setModelId(modelId);
        model.setName(modelFile.getName());
        model.setCommitTime(TimeUtil.currentTime());
        NewModel.addToStorage(model);
    }

    @Override
    public void approveModel(NewModel model, String algorithmName, Algorithm.UpgradeVersion version) {
        checkIfNewModelExist(model);
        Algorithm algorithm = Algorithm.getAlgorithmByName(algorithmName).orElseThrow(() -> (new NotFoundException(String.format("Not found algorithm: %s", algorithmName))));
        ApprovedModel approvedModel = algorithm.approveModel(model, version);
        algorithm.persistApprovalModel(approvedModel);
        NewModel.removeFromStorage(model.getModelId());
    }

    private void cleanGitRepo(Git git, File gitModelRepo) {

        try{
            git.pull().call();
            FileUtils.cleanDirectory(gitModelRepo);
        } catch (IOException e) {
            throw new OcdlException("Git Repo cannot be clean.");
        } catch (Exception e) {
            throw new OcdlException("Fail to git pull");
        }
    }

    private void pushFileToRemoteGitRepo(Git git) {
        try{
            git.add()
                    .addFilepattern(".")
                    .call();
            git.commit()
                    .setMessage("publish model")
                    .call();
            git.push()
                    .call();
        } catch (Exception e){
            throw new OcdlException("Fail to push model");
        }
    }


    private void checkIfNewModelExist(NewModel model) {
        if (!NewModel.existNewModel(model)) {
            throw new NotFoundException("Not Found model:" + model.getName());
        }
    }

    private void throwExceptionIfError(StringBuilder stdErrorOut) throws RuntimeException {
        if (stdErrorOut.length() > 0) {
            throw new RuntimeException(stdErrorOut.toString());
        }
    }


    @Override
    public void rejectModel(NewModel model) {
        checkIfNewModelExist(model);
        Date current = TimeUtil.currentTime();
        RejectedModel.addToStorage(model.convertToRejectedModel());
        NewModel.removeFromStorage(model.getModelId());
    }

    @Override
    public void undo(Model model) {
        if (model instanceof ApprovedModel && !Algorithm.existApprovalModel((ApprovedModel) model)) {
            throw new NotFoundException("model not found");
        }

        if (model instanceof RejectedModel && !RejectedModel.existRejectedModel((RejectedModel) model)) {
            throw new NotFoundException("model not found");
        }

        if (model instanceof NewModel) {
            throw new RuntimeException("can not undo new model");
        }

        NewModel newModel;

        if (model instanceof ApprovedModel) {
            ApprovedModel approvedModel = (ApprovedModel) model;
            Algorithm algorithm = Algorithm.getAlgorithmOfApprovedModel(approvedModel);
            Algorithm.removeApprovedModelFromAlgorithm(algorithm.getAlgorithmName(), approvedModel);
            newModel = approvedModel.convertToNewModel();
        } else {
            RejectedModel.removeFromStorage(model.getModelId());
            newModel = ((RejectedModel) model).convertToNewModel();
        }

        NewModel.addToStorage(newModel);
    }

    @Override
    public void pushModelToGit(Long modelId) {
        ApprovedModel approvedModel = Algorithm.getApprovalModelById(modelId).orElseThrow(() -> (new NotFoundException("Not Found model:" + modelId)));

        //TODO: using git local path
        //clean the git repo first
        String gitRepoPath = Paths.get(CONSTANTS.APPLICATIONS_DIR.GIT_REPO_SPACE, "1/").toString();
        String gitModelPath = Paths.get(CONSTANTS.APPLICATIONS_DIR.GIT_REPO_SPACE, "1/models").toString();

        Git git = null;
        try {
            git = Git.open(new File(gitRepoPath));
        } catch (IOException e) {
            throw new NotFoundException("Git repo not found");
        }
        cleanGitRepo(git, new File(gitModelPath));

        //copy file to git repo
        File modelFile = getModelFileInStage(modelId);
        Optional.ofNullable(modelFile).ifPresent(f -> {
            String targetPath = Paths.get(gitRepoPath,
                    CONSTANTS.NAME_FORMAT.GIT_MODEL.replace("{algorithm}", Algorithm.getAlgorithmOfApprovedModel(approvedModel).getAlgorithmName())
                            .replace("{release_version}", approvedModel.getReleasedVersion().toString())
                            .replace("{cached_version}", approvedModel.getCachedVersion().toString())
                            .replace("{suffix}", f.getName().substring(f.getName().lastIndexOf(".")+1)))
                    .toString();
            try {
                FileUtils.copyFile(f, new File(targetPath));
            } catch (IOException e) {
                throw new OcdlException("Fail to move file to Git repo");
            }
        });

        pushFileToRemoteGitRepo(git);
    }

    /**
     * Find the exact model file in stage
     * @param modelId modelId, as well ad the file name
     * @return
     */
    private File getModelFileInStage(Long modelId) {

        File stage = new File(CONSTANTS.APPLICATIONS_DIR.STAGE_SPACE);
        for (File f : stage.listFiles()) {
            if (!f.isDirectory() && f.getName().startsWith(modelId.toString())) {
                return f;
            }
        }
        return null;
    }

    @Override
    public ModelDto[] getModelsByStatus(Model.Status status) {

        List<ModelDto> modelDtoList = null;

        switch (status) {
            case NEW:
                Model[] newModels = NewModel.getAllNewModels();
                modelDtoList = convertModelsToModelDtoList(newModels);
                break;
            case REJECTED:
                Model[] rejectedModels = RejectedModel.getAllRejectedModels();
                modelDtoList = convertModelsToModelDtoList(rejectedModels);
                break;
            case APPROVED:
                Map<String, Model[]> approvedModelMap = Algorithm.getAllAlgorithmAndModels();
                modelDtoList = convertModelsToModelDtoList(approvedModelMap);
                break;
        }
        return modelDtoList.toArray(new ModelDto[modelDtoList.size()]);
    }

    /**
     * Convert NewModel[] and RejectedModel[] to ModelDto[]
     *
     * @param modelArray
     * @return
     */
    private List<ModelDto> convertModelsToModelDtoList(Model[] modelArray) {

        List<ModelDto> modelDtoList = new ArrayList<>();

        for (Model model : modelArray) {
            ModelDto modelDto = model.convertToModelDto(model);
            modelDtoList.add(modelDto);
        }

        return modelDtoList;
    }


    /**
     * Convert Map<AlgorithmName, ApprovedModel[]> to ModelDto[]
     *
     * @param modelMap
     * @return
     */
    private List<ModelDto> convertModelsToModelDtoList(Map<String, Model[]> modelMap) {

        List<ModelDto> modelDtoList = new ArrayList<>();
        for (String algorithmName : modelMap.keySet()) {
            List<ModelDto> modelDtos = convertModelsToModelDtoList(modelMap.get(algorithmName));
            modelDtoList.addAll(modelDtos);
        }
        return modelDtoList;
    }


    private boolean isModelFile(List<String> modelSuffixes, String fileName) {
        return modelSuffixes.stream().anyMatch(s -> fileName.trim().endsWith(s));
    }
}
