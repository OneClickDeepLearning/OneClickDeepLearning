package acceler.ocdl.controller;

import acceler.ocdl.exception.KuberneteException;
import acceler.ocdl.model.ResourceType;
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

//    private String serverIp;
//
//    @Value("${local.ip}")
//    public void setServerIp(String serverIp) {
//        this.serverIp = serverIp;
//        System.out.println(this.serverIp);
//    }
//
//    @ResponseBody
//    @RequestMapping(params = "status=all", method = RequestMethod.GET)
//    public final Response queryUsingPorts() {
//        return Response.getBuilder()
//                .setCode(Response.Code.SUCCESS)
//                .setData(containerService.getAssignedContainers())
//                .build();
//    }
//
//    @ResponseBody
//    @RequestMapping(params = "status=free", method = RequestMethod.GET)
//    public final Response queryAvailablePortsCount() {
//        return Response.getBuilder()
//                .setCode(Response.Code.SUCCESS)
//                .setData(containerService.getAvailableContainers().size())
//                .build();
//
//    }

    @ResponseBody
    @RequestMapping(path = "/{rscType}", method = RequestMethod.POST)
    public final Response requestContainer(HttpServletRequest request, @PathVariable("rscType") String rscType) {

        User user = (User) request.getAttribute("CURRENT_USER");
        String assign;

        if(getResourceType(rscType).equals(ResourceType.GPU))
            assign = kubernetesService.launchGpuContainer(user);
        else
            assign = kubernetesService.launchCpuContainer(user);

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
//        User user = (User) request.getAttribute("CURRENT_USER");
//        kubernetesService.releaseDockerContainer(getResourceType(rscType),user);
    }

    private ResourceType getResourceType(String rscType){

        if(rscType.equals("gpu"))
            return ResourceType.GPU;
        else if(rscType.equals("cpu"))
            return ResourceType.CPU;
        else
            throw new KuberneteException("Invalid type of resource!");
    }
}
