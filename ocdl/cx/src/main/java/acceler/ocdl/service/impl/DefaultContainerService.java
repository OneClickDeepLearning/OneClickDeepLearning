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
        //System.out.println(this.firstPort);
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

        synchronized (this) {
            for (Integer i : allPorts) {
                if (!assignedContainers.values().contains(i)) {
                    assign = i;
                    break;
                }
            }
        }

        if(user.getType() != 0){
            assign = null;
            return null;
        }

        List<String> cmds = new ArrayList<>();
        StringBuilder cmd = new StringBuilder();
        cmd.append("docker run -dit ");
        cmd.append("-v " + personalDir + user.getUserId().toString() + ":/root/models ");
        cmd.append("-v " + dataDir + ":/root/data ");
        cmd.append("-v /home/ec2-user/CFSC:/root/CFSC ");
        cmd.append("-p " + assign + ":8998 ");
        cmd.append("cpu:1.0 /bin/bash");

        cmds.add(cmd.toString());

        System.out.println(cmd.toString());

	    CmdHelper.runCommand(cmds);


        assignedContainers.put(user,assign);

	try{
		Thread.sleep(3000);
	} catch(Exception ex){
		ex.printStackTrace();
	}
	
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
        //return null;
    }

}
