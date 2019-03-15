package acceler.ocdl.service.impl;

import acceler.ocdl.model.Model;
import acceler.ocdl.model.User;
import acceler.ocdl.persistence.crud.ModelCrud;
import acceler.ocdl.persistence.crud.impl.DefaultModelCrud;
import acceler.ocdl.service.ModelService;
import acceler.ocdl.utils.CmdHelper;
import acceler.ocdl.utils.impl.DefaultCmdHelper;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultModelService implements ModelService {

    @Autowired
    private ModelCrud modelCrud;

    public DefaultModelService(){

    }

    @Override
    public boolean copyModels(User user){

        System.out.println("[debug]" + "in copyModel method");

        String userspace = user.getProjectId().toString() + "-" + user.getUserId().toString();
        String destPath = "/home/ec2-user/stage/";
        File file = new File("/home/hadoop/nfs_hdfs/UserSpace/" + userspace);

        File[] files = file.listFiles();
        if(files == null)
            return false;
        for(File modelFile: files){
            if(modelFile.isDirectory() || !isModelFile(modelFile.getName()))
                continue;
            String srcFileName = modelFile.getName();
            StringBuilder newFileName = new StringBuilder();
            newFileName.append(user.getProjectId().toString());
            newFileName.append("_");
            newFileName.append(user.getUserId().toString());
            newFileName.append("_");
            newFileName.append(srcFileName);

            if(modelFile.renameTo(new File(destPath + newFileName.toString()))){
                Model model = new Model();
                model.setName(newFileName.toString());
                model.setModelTypeId(-1L);
                model.setProjectId(user.getProjectId());
                model.setUrl("/home/ec2-user/stage/" + userspace);
                model.setStatus(Model.Status.NEW);
                modelCrud.createModel(model);
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean pushModel(Model updateModel, String newModelName){

        File file = new File("/home/ec2-user/stage/");
        StringBuilder stderr = new StringBuilder();
        StringBuilder std = new StringBuilder();
        DefaultCmdHelper cmdHelper = new DefaultCmdHelper();

        String repoPath = "/home/ec2-user/models/" + updateModel.getProjectId().toString();

        StringBuilder command = new StringBuilder();
        command.append("cp ");
        command.append(updateModel.getName());
        command.append(" ");
        command.append(repoPath);
        command.append("/");
        command.append(newModelName);

        System.out.println("[debug]" + command.toString());
        cmdHelper.runCommand(file,command.toString(),std,stderr);


        file = new File(repoPath);
        cmdHelper.runCommand(file,"git pull");
        cmdHelper.runCommand(file,"git add .");
        cmdHelper.runCommand(file,"git commit -m \"new model\"");
        cmdHelper.runCommand(file, "git push");

        if(!stderr.toString().equals(""))
            return false;
        return true;
    }

    private boolean isModelFile(String fileName){

        List<String> modelIndex = new ArrayList<>();
        modelIndex.add(".model");

        for(String index : modelIndex){
            if(fileName.endsWith(index))
                return true;
        }
        return false;
    }
}
