package acceler.ocdl.controller;

import acceler.ocdl.utils.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/ping")
public final class PingController {
    @ResponseBody
    @RequestMapping(path = "/ping", method = RequestMethod.GET)
    public final Response ping() {

        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .setMessage("success!")
                .build();

    }


    @ResponseBody
    @RequestMapping(path = "/ping/port", method = RequestMethod.GET)
    public final Response pin(){
        //return CmdHelper.runCommand("docker run -it -n test1 -p 10002:8998 oneclick:jupyterpython /bin/bash &");
//        return "ok";
        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .setMessage("ok")
                .build();
    }
}
