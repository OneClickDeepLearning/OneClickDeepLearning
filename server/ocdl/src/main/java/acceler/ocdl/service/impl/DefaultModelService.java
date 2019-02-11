package acceler.ocdl.service.impl;

import acceler.ocdl.service.ModelService;
import acceler.ocdl.utils.CmdHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultModelService implements ModelService {

    @Override
    public boolean pushModels(List<String> models){

        for (String modelName: models) {
            String s = "git add " + modelName + " && git commit -m \"new models\" && git push";
            System.out.println(s);
	    CmdHelper.runCommand("git add " + modelName);
	    CmdHelper.runCommand("git commit -m \"newmodels\"");
            CmdHelper.runCommand("git push");
	}
        return true;
    }
}
