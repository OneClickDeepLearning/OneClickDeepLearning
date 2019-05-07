package acceler.ocdl.utils;

import java.io.*;

public class SerializationUtils {

    public static void dump(Object target, File persistanceFile) throws RuntimeException {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(persistanceFile);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(target);
        } catch (FileNotFoundException fe) {
            throw new RuntimeException(fe.getMessage());
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



