package acceler.ocdl.controller;


import acceler.ocdl.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/database")
public final class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @ResponseBody
    @RequestMapping(params = "status=getnames", method = RequestMethod.GET)
    public final List<String> queryTemplatesNames(){
        return databaseService.getTemplatesList();
    }

    @ResponseBody
    @RequestMapping(params = "action=getTemplates", method = RequestMethod.POST)
    public final Map<String,String> getTemplates(@RequestBody List<String> ids){
        Map<String,String> templates = new HashMap<>();
        templates = databaseService.getTemplates(ids);
        return templates;
    }
}
