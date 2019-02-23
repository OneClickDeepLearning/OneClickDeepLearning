package acceler.ocdl.controller;

import acceler.ocdl.model.User;
import acceler.ocdl.service.ConfigurationService;
import acceler.ocdl.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
        public final List<String> queryProjectNames() {
            List<String> result=new ArrayList<>();
            result.add(configurationService.RequestProjectName());
            return result;
        }

        @ResponseBody
        @RequestMapping(params = "action=changeProjectName", method = RequestMethod.POST)
        public final List<String> changeProjectNames(@RequestBody Map<String,String> param) {
            List<String> result=new ArrayList<>();
            configurationService.update("project.name",param.get("name"));
            return result;
        }


}
