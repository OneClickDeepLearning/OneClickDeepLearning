package acceler.ocdl.service.impl;

import acceler.ocdl.model.User;
import acceler.ocdl.utils.CmdHelper;
import acceler.ocdl.service.ContainerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class DefaultContainerService implements ContainerService {
    private static final Map<User, Integer> assignedContainers = new ConcurrentHashMap<>();
    private static final Map<Integer, String> models = new ConcurrentHashMap<>();

    private final List<Integer> allPorts;

    private int firstPort = 10000;

    private int lastPort = 12000;

    private final String personalDir = "/home/ec2-user/model_repo/models/";
    private final String dataDir = "/home/ec2-user/data";

    @Value("${local.port.first}")
    public void setFirstPort(int firstPort) {
        this.firstPort = firstPort;
    }

    @Value("${local.port.last}")
    public void setLastPort(int lastPort) {
        this.lastPort = lastPort;
        System.out.println(this.lastPort);
    }

    public DefaultContainerService() {

        this.allPorts = new LinkedList<>();
        List<Integer> unavailablePorts = getUnavailablePorts();

        for (int i = firstPort; i <= lastPort; i++) {
            if (!unavailablePorts.contains(i)) {
                this.allPorts.add(i);
            }
        }
    }

    @Override
    public boolean hasAssignedContainer(final User user) {
        return assignedContainers.containsKey(user);
    }

    @Override
    public Integer getAssignedContainer(final User user) {
        if (!hasAssignedContainer(user)) {
            return null;
        }

        return assignedContainers.get(user);
    }

    @Override
    public List<Integer> getAssignedContainers() {
        List<Integer> assignedPorts = new ArrayList<>();
        for (Integer i : assignedContainers.values()) {
            assignedPorts.add(i);
        }

        return assignedPorts;
    }

    @Override
    public List<Integer> getAvailableContainers() {
        List<Integer> availables = new ArrayList<>();
        for (Integer i : allPorts) {
            if (!assignedContainers.values().contains(i)) {
                availables.add(i);
            }
        }

        return availables;
    }

    @Override
    public Integer requestContainer(final User user){
        if (assignedContainers.containsKey(user)) {
            return assignedContainers.get(user);
        }

        Integer assign = null;

        if(user.getType() != 0){
            assign = null;
            return null;
        }

        StringBuilder cmd = new StringBuilder();

        StringBuilder deploy = new StringBuilder();
        StringBuilder service = new StringBuilder();
        if(user.getUserId() == 1001){
            deploy.append("kubectl create -f /home/ec2-user/minikube/1001deploy.yaml");
            service.append("kubectl create -f /home/ec2-user/minikube/1001svc.yaml");
            assign = 32143;
        } else if(user.getUserId() == 1002){
            deploy.append("kubectl create -f /home/ec2-user/minikube/1002deploy.yaml");
            service.append("kubectl create -f /home/ec2-user/minikube/1002svc.yaml");
            assign = 32144;
        }
        CmdHelper.runCommand(deploy.toString());
        CmdHelper.runCommand(service.toString());
        try {
            Thread.sleep(1000);
        } catch (Exception ex){
            ex.printStackTrace();
        }


        assignedContainers.put(user,assign);

        return assign;

    }

    @Override
    public void releaseContainer(final User user) {
        synchronized (this) {
            assignedContainers.remove(user);
        }
    }


    private List<Integer> getUnavailablePorts() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(8080);
        return list;
    }

}
