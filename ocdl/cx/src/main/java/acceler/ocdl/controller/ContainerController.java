package acceler.ocdl.controller;

import acceler.ocdl.entity.ResourceType;
import acceler.ocdl.entity.User;
import acceler.ocdl.exception.KubernetesException;
import acceler.ocdl.dto.Response;
import acceler.ocdl.service.KubernetesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(path = "/rest/container")
public class ContainerController {

    @Autowired
    private KubernetesService kubernetesService;

    @ResponseBody
    @RequestMapping(path = "/type/{rscType}", method = RequestMethod.POST)
    public final Response requestContainer(HttpServletRequest request, @PathVariable("rscType") String rscType) {
        User user = (User) request.getAttribute("CURRENT_USER");
        String assign;

        try {
            if (getResourceType(rscType).equals(ResourceType.GPU)) {
                assign = kubernetesService.launchGpuContainer(user);
            } else {
                assign = kubernetesService.launchCpuContainer(user);
            }
        }catch (KubernetesException e){
            e.printStackTrace();
            assign = null;
        }

        if (assign == null){
            return Response.getBuilder()
                    .setCode(Response.Code.ERROR)
                    .setMessage("Container launch failed")
                    .build();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("url", assign);

        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .setData(result)
                .build();
    }


/*    @ResponseBody
    @RequestMapping(path = "/release/", method = RequestMethod.DELETE)
    public final void releaseContainer(HttpServletRequest request) {
       InnerUser user = (InnerUser) request.getAttribute("CURRENT_USER");
       kubernetesService.releaseDockerContainer(user);
    }*/

    private ResourceType getResourceType(String rscType) {

        if (rscType.equals("gpu"))
            return ResourceType.GPU;
        else if (rscType.equals("cpu"))
            return ResourceType.CPU;
        else
            throw new KubernetesException("Invalid type of resource!");
    }
}
