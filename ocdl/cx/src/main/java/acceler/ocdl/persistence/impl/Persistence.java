package acceler.ocdl.persistence.impl;

import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.model.ModelType;
import acceler.ocdl.model.Project;
import acceler.ocdl.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Vector;

@Component
class Persistence {

    private static final Logger logger = LoggerFactory.getLogger(Persistence.class);

    // Linux
    public final String projectSerializableFile = "/home/ec2-user/ocdl/OneClickDLTemp/ocdl/cx/src/main/resources/persistence/projectConfiguration";
    public final String userListSerializableFile = "/home/ec2-user/ocdl/OneClickDLTemp/ocdl/cx/src/main/resources/persistence/user";
    public final String modelTypesListSerializableFile = "/home/ec2-user/ocdl/OneClickDLTemp/ocdl/cx/src/main/resources/persistence/modeltypes";

    //windows
//    public final String projectSerializableFile = getClass().getResource("/persistence/projectConfiguration").getPath();
//    public final String userListSerializableFile = getClass().getResource("/persistence/user").getPath();
//    public final String modelTypesListSerializableFile = getClass().getResource("/persistence/modeltypes").getPath();

    private Project project;
    private Vector<User> userList;
    private Vector<ModelType> modelTypes;

//    public Persistence() {}

    public Persistence() {
        logger.debug("project----------------");
        String projectSerializableFileUT8=null;
        String userListSerializableFileUT8=null;
        String modelTypesListSerializableFileUT8=null;

        System.out.println(projectSerializableFile);
        System.out.println(userListSerializableFile);
        System.out.println(modelTypesListSerializableFile);

        try {
            projectSerializableFileUT8 = java.net.URLDecoder.decode(projectSerializableFile,"utf-8");
            userListSerializableFileUT8 = java.net.URLDecoder.decode(userListSerializableFile,"utf-8");
            modelTypesListSerializableFileUT8 = java.net.URLDecoder.decode(modelTypesListSerializableFile,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        this.project = (Project) loadingObject(projectSerializableFileUT8);
        this.userList = (Vector<User>)loadingObject(userListSerializableFileUT8);
        this.modelTypes = (Vector<ModelType>)loadingObject(modelTypesListSerializableFileUT8);
    }

    Project getProject() {
        return project;
    }

    List<User> getUserList() {
        return userList;
    }

    List<ModelType> getModelTypes() {return modelTypes;}

    void persistentUserList() {
        dumpUsers(this.userList, userListSerializableFile);
    }

    void persistentProject(){
        dumpProject(this.project, projectSerializableFile);
    }

    void persistentModelTypes() { dumpModelTypes(this.modelTypes, modelTypesListSerializableFile);}

    private Object loadingObject(String filePath) {
        try {
            logger.debug("file Path: ============================");
            logger.debug(filePath);

            final FileInputStream fileIn = new FileInputStream(filePath);

            final ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            logger.debug("initialize the file object input stream");

            Object object = objectIn.readObject();

            logger.debug("read");

            fileIn.close();
            objectIn.close();
            return object;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new OcdlException("[ERROR] Initialization failed: serializable file loading failed");
        } catch (ClassNotFoundException ex) {
            throw new OcdlException("[ERROR] Initialization failed: Project & UserList class not matched");
        }
    }

    public void dumpProject(Project project, String filePath) {
        String encodePath= null;
        try {
            encodePath= java.net.URLDecoder.decode(projectSerializableFile,"utf-8");
            FileOutputStream fileOut = new FileOutputStream(encodePath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(project);
            fileOut.close();
            objectOut.close();
        } catch (IOException ex) {
            throw new OcdlException("[ERROR] Object dumping failed");
        }
    }

    public void dumpUsers(Vector<User> users, String filePath) {
        String encodePath= null;

        try {
            encodePath= java.net.URLDecoder.decode(projectSerializableFile,"utf-8");
            FileOutputStream fileOut = new FileOutputStream(encodePath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(users);
            fileOut.close();
            objectOut.close();
        } catch (IOException ex) {
            throw new OcdlException("[ERROR] Object dumping failed");
        }
    }

    public void dumpModelTypes(Vector<ModelType> modelTypes, String filePath) {
        String encodePath= null;
        try {
            encodePath= java.net.URLDecoder.decode(projectSerializableFile,"utf-8");
            FileOutputStream fileOut = new FileOutputStream(encodePath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(modelTypes);
            fileOut.close();
            objectOut.close();
        } catch (IOException ex) {
            throw new OcdlException("[ERROR] Object dumping failed");
        }
    }

//    public void createObject(Object object, String filePath) {
//        try {
//            FileOutputStream fileOut = new FileOutputStream(filePath);
//            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
//            objectOut.writeObject(object);
//            fileOut.close();
//            objectOut.close();
//        } catch (IOException ex) {
//            throw new OcdlException("[ERROR] Object dumping failed");
//        }
//    }
//
//    public void createUser(Vector<User> users, String filePath) {
//        try {
//            FileOutputStream fileOut = new FileOutputStream(filePath);
//            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
//            objectOut.writeObject(users);
//            fileOut.close();
//            objectOut.close();
//        } catch (IOException ex) {
//            throw new OcdlException("[ERROR] Object dumping failed");
//        }
//    }
//
//    public void createModelTypes(Vector<ModelType> modelTypes, String filePath) {
//        try {
//            FileOutputStream fileOut = new FileOutputStream(filePath);
//            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
//            objectOut.writeObject(modelTypes);
//            fileOut.close();
//            objectOut.close();
//        } catch (IOException ex) {
//            throw new OcdlException("[ERROR] Object dumping failed");
//        }
//    }


}
