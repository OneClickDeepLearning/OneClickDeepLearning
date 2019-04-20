package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.model.Algorithm;
import acceler.ocdl.model.Model;
import acceler.ocdl.model.Project;
import acceler.ocdl.model.User;
import acceler.ocdl.service.ModelService;
import acceler.ocdl.utils.TimeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DefaultModelServiceImpl implements ModelService {

    private static final Logger log = Logger.getLogger(DefaultModelServiceImpl.class);


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
                        String stagedFilePath = CONSTANTS.NAME_FORMAT.STAGED_MODEL.replace("{FileName}", f.getName()).replace("{timestamp}", TimeUtil.currentTime().toString());
                        FileUtils.moveFile(f, new File(stagedFilePath));
                    }
                }
            } catch (IOException e) {
                log.error(String.format("fail to create new model, because %s failed to move to stage space", files[current].getName()));
            }

            //TODO: persistence
        }

    }

    @Override
    public void approvalModel(Model model, String algorithmName, Algorithm.UpgradeVersion version) {

    }

    @Override
    public void rejectModel(Model model) {

    }

    @Override
    public void undo(Model model) {

    }

    @Override
    public void pushModeltoGit(String source, String newModelName) {

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
