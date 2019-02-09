package acceler.ocdl.controller;


import acceler.ocdl.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
