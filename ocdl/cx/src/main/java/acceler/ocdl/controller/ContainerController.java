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

@Controller
@RequestMapping(path = "/container")
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
    @RequestMapping(params = "action=request", method = RequestMethod.POST)
    public final Response requestContainer(@RequestBody String rscType,HttpServletRequest request) {
//        List<String> result = new ArrayList<>();
        User user = (User) request.getAttribute("CURRENT_USER");

        String assign = kubernetesService.launchDockerContainer(rscType,user);
//        if (assign == null) {
//            result.add("None Container Assigned");
//        } else {
//            result.add(serverIp+":"+assign.toString());
//        }
//        return result;

        if (assign == null) {
            return Response.getBuilder()
                    .setCode(Response.Code.ERROR)
                    .setMessage("None Container Assigned")
                    .build();
        } else {
            return Response.getBuilder()
                    .setCode(Response.Code.SUCCESS)
                    .setData(assign)
                    .build();
        }
    }


//    @ResponseBody
//    @RequestMapping(params = "action=release", method = RequestMethod.POST)
//    public final void releaseContainer(@RequestBody User user) {
//        containerService.releaseContainer(user);
//    }
}
