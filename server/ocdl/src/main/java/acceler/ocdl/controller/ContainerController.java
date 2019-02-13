package acceler.ocdl.controller;

import acceler.ocdl.model.User;
import acceler.ocdl.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/container")
public final class ContainerController {

    @Autowired
    private ContainerService containerService;

    private String serverIp;

    @Value("${local.ip}")
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
        System.out.println(this.serverIp);
    }

    @ResponseBody
    @RequestMapping(params = "status=all", method = RequestMethod.GET)
    public final List<Integer> queryUsingPorts() {
        return containerService.getAssignedContainers();
    }

    @ResponseBody
    @RequestMapping(params = "status=free", method = RequestMethod.GET)
    public final Integer queryAvailablePortsCount() {
        return containerService.getAvailableContainers().size();
    }

    @ResponseBody
    @RequestMapping(params = "action=request", method = RequestMethod.POST)
    public final List<String> requestContainer(@RequestBody User user) {
        List<String> result = new ArrayList<>();
        Integer assign = containerService.requestContainer(user);
        if (assign == null) {
            result.add("None Container Assigned");
        } else {
            result.add(serverIp+":"+assign.toString());
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(params = "action=release", method = RequestMethod.POST)
    public final void releaseContainer(@RequestBody User user) {
        containerService.releaseContainer(user);
    }
}
