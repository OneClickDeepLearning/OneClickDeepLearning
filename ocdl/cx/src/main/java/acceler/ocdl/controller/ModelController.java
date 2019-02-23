package acceler.ocdl.controller;

import acceler.ocdl.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/model")
public final class ModelController {

    @Autowired
    private ModelService modelService;

    @ResponseBody
    @RequestMapping(params = "action=push", method = RequestMethod.POST)
    public final List<String> queryPushModels(@RequestBody String userId) {
        List<String> result = new ArrayList<>();
        if(modelService.pushModels(userId)) {
            result.add("push succeeded");
        } else{
            result.add("push failed");
        }
        return result;
    }
}
