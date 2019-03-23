package acceler.ocdl.service;

import acceler.ocdl.model.Project;

public interface ConfigurationService {
    public String RequestProjectName();
    public Project RequestAllConfigurationInfo();
    public void updateProject(Project project);
}
