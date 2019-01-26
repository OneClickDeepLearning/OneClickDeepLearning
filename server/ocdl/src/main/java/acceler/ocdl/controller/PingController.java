package acceler.ocdl.controller;

import acceler.ocdl.service.CmdHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public final class PingController {

    @ResponseBody
    @RequestMapping(path = "/ping", method = RequestMethod.GET)
    public final String ping() {
        return "success!";
    }


    @ResponseBody
    @RequestMapping(path = "/pin", method = RequestMethod.GET)
    public final String pin() {
        String s = CmdHelper.runCommand("docker run -it -n test1 -p 10002:8998 oneclick:jupyterpython /bin/bash &");
        return s;
    }
}
