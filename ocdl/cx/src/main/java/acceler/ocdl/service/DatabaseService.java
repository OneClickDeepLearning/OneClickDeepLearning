package acceler.ocdl.service;

import acceler.ocdl.model.Model;
import acceler.ocdl.model.Project;
import acceler.ocdl.model.Template;
import acceler.ocdl.model.User;

import java.util.ArrayList;

public interface DatabaseService {

    int createNewRole(User.Role role);

    int getRoleId(User.Role role);

    String getRoleName(int roleId);

    Long getUserId(String userName);

    User getUserInfo(String userName);

    String getUserPassword(String userName);

    int createProject(String projectName, String description);

    int getProjectId(String projectName);

    Project getProjectInfo(String projectName);

    Project getProjectInfo(int projectId);

    void setProjectGit(String git, int projectId);

    void setProjectK8(String k8Url, int projectId);

    void setProjectTemplate(String templateUrl, int projectId);

    void createUserProjectRelation(User user, String projectName);

    ArrayList<Project> getProjectList(Long userId);

    User getProjectManager(int projectId);

    int createModelType(String modelTypeName, int projectId);

    ArrayList<String> getModelType(int projectId);

    int getModelTypeId(int projectId, String modelTypeName);

    int getStatusId(Model.Status status);

    int createModel(Model model);

    void updateModelStatus(Model model, Model.Status expectedStatus);

    void updateModelVersion(Model model, String version);

    ArrayList<Model> getAllProjectModel(String projectName);

    ArrayList<Model> getConditioanalProjectModel(String projectName, Model.Status condition);

    int createTemplate(Template template);

    Template getTemplateInfo(Long templateId);

    Template getTemplateInfo(String templateName);
}
