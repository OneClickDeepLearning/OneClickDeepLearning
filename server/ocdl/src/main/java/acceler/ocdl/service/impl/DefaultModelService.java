package acceler.ocdl.service.impl;

import acceler.ocdl.service.ModelService;
import acceler.ocdl.utils.CmdHelper;

import java.util.List;

public class DefaultModelService implements ModelService {

    @Override
    public boolean pushModels(List<String> models) {

        CmdHelper.runCommand("cd /root/model_repo/models");

        for (String modelName: models) {
            CmdHelper.runCommand("git add " + modelName);
        }

        CmdHelper.runCommand("git commit -m \"new models\"");
        CmdHelper.runCommand("git push");

        return true;
    }
}
