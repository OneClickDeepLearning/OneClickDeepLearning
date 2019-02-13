package acceler.ocdl.service.impl;

import acceler.ocdl.service.ModelService;
import acceler.ocdl.utils.CmdHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultModelService implements ModelService {

    @Override
    public boolean pushModels(String userId){

        CmdHelper.runCommand("cd /home/ec2-user/model_repo/models/" + userId + "/ && ");

        CmdHelper.runCommand("git add .");

	    CmdHelper.runCommand("git commit -m \"newmodels\"");
        CmdHelper.runCommand("git push");
        return true;
    }
}
