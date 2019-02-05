package acceler.ocdl.service;

import acceler.ocdl.model.User;

import java.util.List;

public interface ContainerService {

    public boolean hasAssignedContainer(User user);

    public Integer getAssignedContainer(User user);

    public List<Integer> getAssignedContainers();

    public List<Integer> getAvailableContainers();

    public List<String> getModelFiles();

    public Integer requestContainer(User user);

    public void releaseContainer(User user);



}
