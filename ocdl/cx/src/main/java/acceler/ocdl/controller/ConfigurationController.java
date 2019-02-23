package acceler.ocdl.controller;


import acceler.ocdl.service.ConfigurationService;
import acceler.ocdl.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/configuration")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @ResponseBody
    @RequestMapping(params = "action=project", method = RequestMethod.GET)
    public final Response queryProjectNames() {
        List<String> result = new ArrayList<>();
        result.add(configurationService.RequestProjectName());
        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .setData(result)
                .build();
    }

    @ResponseBody
    @RequestMapping(params = "action=changeProjectName", method = RequestMethod.POST)
    public final List<String> changeProjectNames(@RequestBody Map<String, String> param) {
        List<String> result = new ArrayList<>();
        configurationService.update("project.name", param.get("name"));
        return result;
    }
}
