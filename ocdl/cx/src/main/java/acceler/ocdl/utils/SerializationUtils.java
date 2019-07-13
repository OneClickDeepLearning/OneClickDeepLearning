package acceler.ocdl.utils;

import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;

public class SerializationUtils {

    public static final Logger logger = LoggerFactory.getLogger(SerializationUtils.class);

    @Value("${data.path}")
    public static String defaultDataPath;

    public static boolean existDefaultSerializedFile() {

        boolean existSerializedFile = true;
        File persistanceFile = new File(defaultDataPath);

        if (!persistanceFile.exists()) {
            existSerializedFile = false;
        }
        return existSerializedFile;
    }

    public static void createSerializedFile(String path) {

        File persistanceFile = new File(path);
        try {
            persistanceFile.createNewFile();
        } catch (IOException ie) {
            throw new RuntimeException(ie.getMessage());
        }
    }


    public static void dump(Object target, File persistanceFile) throws RuntimeException {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;


        if (!persistanceFile.exists()) {
//            logger.info("[Serialization] create a serialization file:" + persistanceFile);
//                persistanceFile.createNewFile();
            throw new NotFoundException("PersistanceFile not found");
        }

        try {
            fileOutputStream = new FileOutputStream(persistanceFile);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(target);
        } catch (IOException ie) {
            throw new RuntimeException(ie.getMessage());
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException ioe) {
                throw new RuntimeException(ioe.getMessage());
            }
        }
    }

    public static Object load(File sourceFile) throws RuntimeException {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        Object object = null;

        try {
            fileInputStream = new FileInputStream(sourceFile);
            objectInputStream = new ObjectInputStream(fileInputStream);
            object = objectInputStream.readObject();
        } catch (FileNotFoundException fe) {
            throw new RuntimeException(fe.getMessage());
        } catch (IOException ie) {
            throw new RuntimeException(ie.getMessage());
        } catch (ClassNotFoundException ce) {
            throw new RuntimeException(ce.getMessage());
        } finally {
            try {
                fileInputStream.close();
                objectInputStream.close();
            } catch (IOException ie) {
                throw new RuntimeException(ie.getMessage());
            }
        }
        if (object == null) {
            throw new RuntimeException("load object is null");
        }

        return object;
    }
}



