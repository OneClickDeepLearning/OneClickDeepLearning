package acceler.ocdl.persistence.impl;

import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.model.ModelType;
import acceler.ocdl.model.Project;
import acceler.ocdl.model.User;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Vector;

@Component
class Persistence {

    public final static String projectSerializableFile = "";
    public final static String userListSerializableFile = "";

    private Project project;
    private Vector<User> userList;
    private Vector<ModelType> modelTypes;

    public Persistence() {
        this.project = (Project) loadingObject(projectSerializableFile);
        this.userList = (Vector<User>)loadingObject(userListSerializableFile);
    }

    Project getProject() {
        return project;
    }

    List<User> getUserList() {
        return userList;
    }

    List<ModelType> getModelTypes() {return modelTypes;}

    void persistentUserList() {
        dumpObject(this.userList, userListSerializableFile);
    }

    void persistentProject(){
        dumpObject(this.project, projectSerializableFile);
    }

    private Object loadingObject(String filePath) {
        try {
            final FileInputStream fileIn = new FileInputStream(filePath);
            final ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object object = objectIn.readObject();
            fileIn.close();
            objectIn.close();
            return object;
        } catch (IOException ex) {
            throw new OcdlException("[ERROR] Initialization failed: serializable file loading failed");
        } catch (ClassNotFoundException ex) {
            throw new OcdlException("[ERROR] Initialization failed: Project & UserList class not matched");
        }
    }

    public void dumpObject(Object object, String filePath) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            fileOut.close();
            objectOut.close();
        } catch (IOException ex) {
            throw new OcdlException("[ERROR] Object dumping failed");
        }
    }
}
