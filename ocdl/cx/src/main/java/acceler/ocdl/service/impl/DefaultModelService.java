package acceler.ocdl.service.impl;

import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.Model;
import acceler.ocdl.model.User;
import acceler.ocdl.persistence.ModelCrud;
import acceler.ocdl.persistence.ProjectCrud;
import acceler.ocdl.service.ModelService;
import acceler.ocdl.utils.impl.DefaultCmdHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.io.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class DefaultModelService implements ModelService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultModelService.class);
    private static final String userspacePath = "/home/hadoop/nfs_hdfs/UserSpace/";
    private static final String stagePath = "/home/ec2-user/stage/";
    private static final String gitRepoPath = "/home/ec2-user/models/";

    @Autowired
    private ModelCrud modelCrud;

    @Autowired
    private ProjectCrud projectCrud;

    public DefaultModelService(){ }

    @Override
    public boolean copyModels(User user){

        logger.debug("[debug]" + "in copyModel method");
        String userspace = projectCrud.getProjectName() + "-" + user.getUserId();

        // FIXME: hadoop file system
        File file =  null; //new File("/home/hadoop/nfs_hdfs/UserSpace/" + userspace);

        File[] files = file.listFiles();
        if(files == null) {
            return false;
        }

        for(File modelFile: files){
            if(modelFile.isDirectory() || !isModelFile(modelFile.getName()))
                continue;
            String srcFileName = modelFile.getName();
            StringBuilder newFileName = new StringBuilder();
/*            newFileName.append(user.getProjectId().toString());*/
            newFileName.append("_");
/*            newFileName.append(user.getUserId().toString());*/
            newFileName.append("_");
            newFileName.append(srcFileName);

            logger.debug("[debug]" + srcFileName);
            logger.debug("[debug]" + newFileName);

            long time = new Date().getTime();
            String targetPath = Paths.get(stagePath, "new", modelFile.getName(), "_", String.valueOf(time)).toString();

            try {
                FileUtils.moveFile(modelFile,new File(targetPath));
            } catch (IOException e){
                //FIXME: throw exception at first
                logger.error("Fail to move file.");
                return false;
            }

//            //FIXME: if moving file fails, doesn't write to DB
//            Model model = new Model();
//            model.setName(newFileName.toString());
//            model.setModelTypeId(-1L);
//            model.setUrl("/home/ec2-user/stage/" + userspace);*/
//            model.setStatus(Model.Status.NEW);
//            modelCrud.createModel(model);
        }
        return true;
    }

    @Override
    public boolean pushModel(String sourcePath, String newModelName){

        String repoPath = Paths.get(gitRepoPath, projectCrud.getProjectName()).toString();


        File stageFile = new File(sourcePath);
        if (stageFile == null) {
            logger.error("Cannot find the model that need to be pushed.");
            throw new NotFoundException("Cannot find the model that need to be pushed.", "Cannot find the model that need to be pushed.");
        }


        try {
            FileUtils.copyFile(stageFile,new File(repoPath + "/" + newModelName));
        } catch (IOException e){

            logger.error(e.getMessage());
            //FIXME: throw one self-defined exception

            return false;
        }

        DefaultCmdHelper cmdHelper = new DefaultCmdHelper();

        File file = new File(repoPath);
        if (file == null) {
            logger.error("Cannot find pushed model in git repo.");
            throw new NotFoundException("Cannot find pushed model in git repo.", "Cannot find pushed model in git repo.");
        }

        StringBuilder stderr = new StringBuilder();
        StringBuilder std = new StringBuilder();
        cmdHelper.runCommand(file,"git pull",std,stderr);
        cmdHelper.runCommand(file,"git add --all",std,stderr);
        cmdHelper.runCommand(file,"git commit -m \"newmodel\"",std,stderr);
        cmdHelper.runCommand(file, "git push",std,stderr);

        if(!stderr.toString().equals("")) {
            System.out.println(stderr.toString());
            return false;
        }
        return true;
    }

    private boolean isModelFile(String fileName){

        //FIXME: private static final, 从文件读取这个配置
        String[] suffixArray = projectCrud.getProjectConfiguration().getSuffix().split(";");
        List<String> modelIndex = new ArrayList<>();
        for (String s: suffixArray) {
            if (!s.trim().equals("")) {
                modelIndex.add(s);
            }
        }

        for(String index : modelIndex){
            if(fileName.endsWith(index))
                return true;
        }
        return false;
    }

    @Override
    public List<ModelDto> getModels(String status) {

        List<ModelDto> modelDtos = new ArrayList<>();

        String stageNewPath = Paths.get(stagePath, status).toString();
        logger.debug(stageNewPath);

        File file = new File(stageNewPath);
        File[] files = file.listFiles();
        if(file == null || files == null) {
            logger.debug("files is none");
            return null;
        }

        for (File f : files) {


            ModelDto modelDto = parseFileName(f.getName());
            modelDto.setStatus(status);

            modelDtos.add(modelDto);
        }

        return modelDtos;
    }

    @Override
    public boolean modelExist(String modelName, String preStatus) {

        String stageNewPath = Paths.get(stagePath, preStatus).toString();

        File file = new File(stageNewPath);
        File[] files = file.listFiles();
        if(files == null) {
            return false;
        }

        Boolean modelExist = false;
        for (File f : files) {
            if (f.getName().equals(modelName)) {
                modelExist = true;
                break;
            }
        }
        return modelExist;
    }

    @Override
    public boolean moveFile(Path source, Path target) throws IOException{

        Path temp = Files.move(source, target);

        if (temp == null) {
            logger.error("Failed to move and rename file");
            return false;
        }

        logger.info("File renamed and moved successfully");
        return true;
    }


    private ModelDto parseFileName(String fileName) {

        ModelDto modelDto = new ModelDto();

        // remove suffix
        int posDot = fileName.indexOf(".");
        if (posDot >= 0) {
            fileName = fileName.substring(0, posDot);
        }

        String[] modelInfo = fileName.trim().split("_");

        if (modelInfo.length == 2) {
            modelDto.setModelName(modelInfo[0]);
            modelDto.setTimeStamp(modelInfo[1]);
            modelDto.setModelType("");
            modelDto.setVersion("");
        } else {
            modelDto.setModelName(modelInfo[0]);
            modelDto.setTimeStamp(modelInfo[1]);
            modelDto.setModelType(modelInfo[2]);
            modelDto.setVersion(modelInfo[3]);
        }

        return modelDto;

    }

}
