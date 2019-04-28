package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.*;
import acceler.ocdl.service.ModelService;
import acceler.ocdl.utils.CommandHelper;
import acceler.ocdl.utils.TimeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class DefaultModelServiceImpl implements ModelService {

    private static final Logger log = Logger.getLogger(DefaultModelServiceImpl.class);

    @Autowired
    private CommandHelper commandHelper;

    @Override
    public void initModelToStage(User user) {
        final String userSpaceName = CONSTANTS.NAME_FORMAT.USER_SPACE.replace("{projectName}", Project.getProjectName()).replace("{{userId}}", String.valueOf(user.getUserId()));
        final File userSpace = new File(userSpaceName);

        if (userSpace.isDirectory()) {
            File[] files = userSpace.listFiles();
            List<String> suffixes = Arrays.asList(Project.getModelFileSuffixes());

            //IO error occures
            if (files == null) {
                log.error(String.format("fail in reading directory: %s", userSpaceName));
                return;
            }

            int current = 0;

            try {
                for (; current < files.length; current++) {
                    File f = files[current];

                    if (!f.isDirectory() && isModelFile(suffixes, f.getName())) {
                        String stagedFilePath = CONSTANTS.NAME_FORMAT.STAGED_MODEL.replace("{fileName}", f.getName()).replace("{timestamp}", TimeUtil.currentTime().toString());
                        FileUtils.moveFile(f, new File(stagedFilePath));
                    }

                    persistNewModel(f);
                }
            } catch (IOException e) {
                log.error(String.format("fail to create new model, because %s failed to move to stage space", files[current].getName()));
            }
        }
    }

    private void persistNewModel(File modelFile) {
        NewModel model = new NewModel();
        model.setName(modelFile.getName());
        model.setCommitTime(TimeUtil.currentTime());
        NewModel.addToStorage(model);
    }

    @Override
    public void approveModel(NewModel model, String algorithmName, Algorithm.UpgradeVersion version) {
        checkIfNewModelExist(model);

        Algorithm algorithm = Algorithm.getAlgorithmByName(algorithmName).orElseThrow(() -> (new NotFoundException("Not found algorithm:" + algorithmName, "algorithm not found")));

        ApprovedModel approvedModel = algorithm.approveModel(model, version);



        algorithm.persistApprovalModel(approvedModel);

        NewModel.removeFromStorage(model.getName());
    }

    private void pushFileToRemoteGitRepo(File workDir) {
        StringBuilder stdErrOut = new StringBuilder();
        StringBuilder stdOut = new StringBuilder();

        commandHelper.runCommand(workDir, "git pull", stdOut, stdErrOut);
        throwExceptionIfError(stdErrOut);

        commandHelper.runCommand(workDir, "git add --all", stdOut, stdErrOut);
        throwExceptionIfError(stdErrOut);

        commandHelper.runCommand(workDir, "git commit -m \"approved models\"", stdOut, stdErrOut);
        throwExceptionIfError(stdErrOut);

        commandHelper.runCommand(workDir, "git push", stdOut, stdErrOut);
        throwExceptionIfError(stdErrOut);
    }


    private void checkIfNewModelExist(NewModel model) {
        if (!NewModel.existNewModel(model)) {
            throw new NotFoundException("Not Found model:" + model.getName(), "Model Not Found");
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
        NewModel.removeFromStorage(model.getName());
    }

    @Override
    public void undo(Model model) {
        if (model instanceof ApprovedModel && Algorithm.approvalModelExist((ApprovedModel) model)) {
            throw new NotFoundException("", "");
        }

        if (model instanceof RejectedModel && RejectedModel.existRejectedModel((RejectedModel) model)) {
            throw new NotFoundException("", "");
        }

        if (model instanceof NewModel) {
            throw new RuntimeException("can not undo new model");
        }

        NewModel newModel;

        if (model instanceof ApprovedModel) {
            ApprovedModel approvedModel = (ApprovedModel) model;
            Algorithm algorithm = Algorithm.getAlgorithmOfApprovedModel(approvedModel);
            algorithm.removeApprovedModelFromAlgorithm(approvedModel);
            newModel = approvedModel.convertToNewModel();
        } else {
            RejectedModel.removeFromStorage(model.getName());
            newModel = ((RejectedModel) model).convertToNewModel();
        }

        NewModel.addToStorage(newModel);
    }

    @Override
    public void pushModelToGit(String modelName) {
        ApprovedModel approvedModel = Algorithm.getApprovalModelByName(modelName).orElseThrow(() -> (new NotFoundException("Not Found model:" + modelName, "Not found model")));
        //TODO: move file to git repo
        //TODO: read File space from Project.gitrepo
        pushFileToRemoteGitRepo("{gitrepo}");
    }

    @Override
    public List<Model> getModelsByStatus(Model.Status status) {
        return null;
    }

    @Override
    public boolean existModel(String modelName, Model.Status status) {
        return false;
    }


    private boolean isModelFile(List<String> modelSuffixes, String fileName) {
        return modelSuffixes.stream().anyMatch(s -> fileName.trim().endsWith(s));
    }
}
