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
import org.apache.commons.io.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultModelService implements ModelService {

    @Autowired
    private ModelCrud modelCrud;

    public DefaultModelService(){ }

    @Override
    public boolean copyModels(User user){

        System.out.println("[debug]" + "in copyModel method");
/*        String userspace = user.getProjectId().toString() + "-" + user.getUserId().toString();*/
        //FIXME: 该对象必须是 private static final
        String destPath = "/home/ec2-user/stage/";
        //FIXME: private static final String : "/home/hadoop/nfs_hdfs/UserSpace/"
        File file =  null; //new File("/home/hadoop/nfs_hdfs/UserSpace/" + userspace);

        File[] files = file.listFiles();
        //FIXME: 少用单行缺{} 语法糖
        if(files == null)
            return false;
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

            System.out.println("[debug]" + srcFileName);
            System.out.println("[debug]" + newFileName);

            try {
                FileUtils.moveFile(modelFile,new File(destPath + newFileName.toString()));
            } catch (IOException e){
                //FIXME: throw exception at first
                return false;
            }
            //FIXME: if moving file fails, doesn't write to DB
            Model model = new Model();
            model.setName(newFileName.toString());
            model.setModelTypeId(-1L);
/*            model.setProjectId(user.getProjectId());
            model.setUrl("/home/ec2-user/stage/" + userspace);*/
            model.setStatus(Model.Status.NEW);
            modelCrud.createModel(model);
        }
        return true;
    }

    public boolean pushModel(Model updateModel, String newModelName){
        //FIXME: private static final  "/home/ec2-user/models/"
/*        String repoPath = "/home/ec2-user/models/" + updateModel.getProjectId().toString();*/
        //FIXME: need to check file.exists(), throw NotFoundException
        File stageFile = new File("/home/ec2-user/stage/" + updateModel.getName());
/*        try {
            FileUtils.copyFile(stageFile,new File(repoPath + "/" + newModelName));
        } catch (IOException e){
            //FIXME: throw one self-defined exception
            System.out.println("[debug]" + e.getMessage());
            return false;
        }*/

        DefaultCmdHelper cmdHelper = new DefaultCmdHelper();
/*        File file = new File(repoPath);*/
        //FIXME: check file.exists()
        StringBuilder stderr = new StringBuilder();
        StringBuilder std = new StringBuilder();
/*        cmdHelper.runCommand(file,"git pull",std,stderr);
        cmdHelper.runCommand(file,"git add --all",std,stderr);
        cmdHelper.runCommand(file,"git commit -m \"newmodel\"",std,stderr);
        cmdHelper.runCommand(file, "git push",std,stderr);*/

//        if(!stderr.toString().equals("")) {
//            System.out.println(stderr.toString());
//            return false;
//        }
        return true;
    }

    private boolean isModelFile(String fileName){

        //FIXME: private static final, 从文件读取这个配置
        List<String> modelIndex = new ArrayList<>();
        modelIndex.add(".model");

        for(String index : modelIndex){
            if(fileName.endsWith(index))
                return true;
        }
        return false;
    }
}
