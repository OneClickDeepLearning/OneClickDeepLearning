package acceler.ocdl.service.impl;

import acceler.ocdl.service.ModelService;
import acceler.ocdl.utils.CmdHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultModelService implements ModelService {

    @Override
    public boolean pushModels(List<String> models){

        CmdHelper.runCommand("cd /home/ec2-user/model_repo/models/1001/");

        for (String modelName: models) {
            CmdHelper.runCommand("git add " + modelName);
        }

        CmdHelper.runCommand("git commit -m \"new models\"");
        CmdHelper.runCommand("git push");

        return true;
    }
}
