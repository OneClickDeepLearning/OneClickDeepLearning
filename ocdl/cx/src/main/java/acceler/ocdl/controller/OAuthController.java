package acceler.ocdl.controller;

import acceler.ocdl.dto.Response;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(path = "/oauth")
public class OAuthController {

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Response login(@RequestBody Map<String,String> param) {
        final Response.Builder respBuilder = Response.getBuilder();
        System.out.println(param.get("id"));
        respBuilder.setCode(Response.Code.SUCCESS);
        return respBuilder.build();
    }
}
