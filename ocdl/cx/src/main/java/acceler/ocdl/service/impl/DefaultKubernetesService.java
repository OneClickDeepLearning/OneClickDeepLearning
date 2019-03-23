package acceler.ocdl.service.impl;

import acceler.ocdl.exception.KuberneteException;
import acceler.ocdl.model.User;
import acceler.ocdl.service.KubernetesService;
import acceler.ocdl.utils.impl.DefaultCmdHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultKubernetesService implements KubernetesService {

    private static final Map<User, String> cpuAssigned = new ConcurrentHashMap<>();
    private static final Map<User, String> gpuAssigned = new ConcurrentHashMap<>();
    //private static final Map<Integer, String> models = new ConcurrentHashMap<>();
    private static final Map<String,String> ipMap = new HashMap<>();

    public DefaultKubernetesService() {
        ipMap.put("10.8.0.1", "3.89.28.106");
        ipMap.put("10.8.0.6", "3.87.64.159");
        ipMap.put("10.8.0.10", "66.131.186.246");
    }

    //FIXME: 建议这个方法在任何情况下都不返回null, 在null 情况下 throw exception
    public String launchDockerContainer(String rscType, User user) throws KuberneteException{
        //FIXME: 定义EnumType,在controller做string到 enum的转化
        if(rscType.equals("cpu") && cpuAssigned.containsKey(user))
            return cpuAssigned.get(user);
        else if(rscType.equals("gpu") && gpuAssigned.containsKey(user))
            return gpuAssigned.get(user);
        else if(!rscType.equals("gpu") && !rscType.equals("cpu"))
            return null;
        else if(rscType.equals("gpu") && gpuAssigned.size() == 1)
            return null;

        String url = null;
        String ip;
        String port;
        String nameSpace = user.getProjectId().toString() + "-" + user.getUserId().toString();

        //FIXME: bean, 不要自己创建
        DefaultCmdHelper cmdHelper = new DefaultCmdHelper();

        StringBuilder std = new StringBuilder();
        StringBuilder stderr = new StringBuilder();
        //FIXME: private static final
        File file = new File("/home/ec2-user/k8s/deployment");
        //stderr = new StringBuilder();
        //std = new StringBuilder();

        StringBuilder command = new StringBuilder();
        command.append("sh ").append(rscType).append("_makeDeploy.sh ").append(nameSpace);
        cmdHelper.runCommand(file,command.toString(), std, stderr);
        command = new StringBuilder();
        command.append("kubectl create -f ").append(nameSpace).append("-deploy-").append(rscType).append(".yaml");
        cmdHelper.runCommand(file, command.toString(),std,stderr);

        std = new StringBuilder();
        command = new StringBuilder();
        command.append("sh getIp.sh ").append(nameSpace).append(" ").append(rscType);
        cmdHelper.runCommand(file, command.toString(), std, stderr);
        ip = ipMap.get(std.toString());
        std = new StringBuilder();
        command = new StringBuilder();
        command.append("sh getPort.sh ").append(nameSpace).append(" ").append(rscType);
        cmdHelper.runCommand(file, command.toString(), std, stderr);
        port = std.toString();

        if(!stderr.toString().equals("")){
            System.out.println(stderr.toString());
            throw new KuberneteException(stderr.toString());
        }

        url = ip + ":" + port;

        //FIXME: 建议重写user
        //FIXME: synchronized ? , cocurrentHashMap 本身已经线程安全
        if(rscType.equals("cpu")) {
            synchronized (this) {
                cpuAssigned.put(user, url);
            }
        } else {
            synchronized (this) {
                gpuAssigned.put(user, url);
            }
        }

        System.out.println(url);
        return url;
    }

    public void releaseDockerContainer(String rscType, User user) throws KuberneteException{

    }
}
