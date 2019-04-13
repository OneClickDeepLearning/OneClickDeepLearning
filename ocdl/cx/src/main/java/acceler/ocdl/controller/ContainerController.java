package acceler.ocdl.controller;

import acceler.ocdl.model.User;
import acceler.ocdl.service.ContainerService;
import acceler.ocdl.dto.Response;
import acceler.ocdl.service.KubernetesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/rest/container")
public final class ContainerController {

    @Autowired
    private KubernetesService kubernetesService;

    @ResponseBody
    @RequestMapping(path = "/type/{rscType}", method = RequestMethod.POST)
    public final Response requestContainer(HttpServletRequest request, @PathVariable("rscType") String rscType) {

        User user = (User) request.getAttribute("CURRENT_USER");

        String assign = kubernetesService.launchDockerContainer(rscType,user);
//        if (assign == null) {
//            result.add("None Container Assigned");
//        } else {
//            result.add(serverIp+":"+assign.toString());
//        }
//        return result;

        if(assign == null)
            return Response.getBuilder()
                    .setCode(Response.Code.ERROR)
                    .setMessage("Container launch failed")
                    .build();

        Map<String, Object> result = new HashMap<>();
        result.put("url",assign);

        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .setData(result)
                .build();
    }


    @ResponseBody
    @RequestMapping(params = "/release/", method = RequestMethod.DELETE)
    public final void releaseContainer(@RequestBody String rscType,HttpServletRequest request) {
        User user = (User) request.getAttribute("CURRENT_USER");
        kubernetesService.releaseDockerContainer(rscType,user);
    }
}
