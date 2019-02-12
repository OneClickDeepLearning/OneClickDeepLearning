package acceler.ocdl.controller;


import acceler.ocdl.service.TemplateService;
import com.sun.org.apache.xerces.internal.xs.StringList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/template")
public final class TemplateController {

    @Autowired
    private TemplateService databaseService;

    @ResponseBody
    @RequestMapping(params = "status=getnames", method = RequestMethod.GET)
    public final List<List<String>> queryTemplatesNames(){
        List<List<String>> result = new ArrayList<List<String>>();
        result.add(databaseService.getTemplatesList("Blocks"));
        result.add( databaseService.getTemplatesList("Layers"));
        result.add(  databaseService.getTemplatesList("Networks"));
        return result;
    }

    @ResponseBody
    @RequestMapping(params = "action=getTemplates", method = RequestMethod.POST)
    public final Map<String,String> getTemplates(@RequestBody List<String> ids){
        Map<String,String> templates = new HashMap<>();
        templates = databaseService.getTemplates(ids);
        return templates;
    }
    @ResponseBody
    @RequestMapping(params = "action=testTemplates", method = RequestMethod.POST)
    public final List<String> testTemplates(@RequestBody Map<String,String> param){
        List<String> templates = new ArrayList <String>();
        templates = databaseService.getTemplates2(param.get("name"),param.get("type"));
        return templates;
    }
}
