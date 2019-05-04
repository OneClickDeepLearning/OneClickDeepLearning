package acceler.ocdl.service;

import java.io.File;


public interface StorageService {

    void createStorage();

    void uploadObject(String bucketName, String modelName, File file);

    String getObkectUrl(String bucketName, String objectKey);

    void downloadObject(String bucketName, String ObjectKey);
}
