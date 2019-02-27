package acceler.ocdl.service;

import acceler.ocdl.model.Model;
import acceler.ocdl.model.Project;
import acceler.ocdl.model.Template;
import acceler.ocdl.model.User;

import java.util.ArrayList;

public interface DatabaseService {
    /*
     * insert a new role in the table user_role
     */
    int createNewRole(User.Role role);

    /*
     * get the role id in the table user_role
     */
    int getRoleId(User.Role role);

    /*
     * get the role id in the table user_role
     */
    String getRoleName(int roleId);

    /*
     * Get user info except the password
     */
    Long getUserId(String userName);

    /*
     * Get user info except the password
     */
    User getUserInfo(String userName);

    /*
     * get the user password
     * TODO: password need to be encrypted
     */
    String getUserPassword(String userName);

    /*
     * insert a new project in the table project
     */
    int createProject(String projectName, String description);

    /*
     * get the project id in the table project
     */
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

    /*
     * get the model type id in the table model_type
     */
    int getModelTypeId(int projectId, String modelTypeName);

    /*
     * get status id in the table status
     */
    int getStatusId(Model.Status status);

    int createModel(Model model);

    void updateModelStatus(Model model, Model.Status expectedStatus);

    /*
     * TODO: big version or small version
     */
    void updateModelVersion(Model model, String version);

    ArrayList<Model> getAllProjectModel(String projectName);

    /*
     * get project models on specific status
     * @para condition specific status, such as: new/approval/reject
     */
    ArrayList<Model> getConditioanalProjectModel(String projectName, Model.Status condition);

    int createTemplate(Template template);

    Template getTemplateInfo(Long templateId);

    Template getTemplateInfo(String templateName);
}
