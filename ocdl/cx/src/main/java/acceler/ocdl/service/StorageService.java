package acceler.ocdl.service;

import java.io.File;


public interface StorageService {

    public void createStorage();

    public void uploadObject(String bucketName, String modelName, File file);

    public String getObkectUrl(String bucketName, String objectKey);

    public void downloadObject(String bucketName, String ObjectKey);
}
