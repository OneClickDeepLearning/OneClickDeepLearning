package acceler.ocdl.service;

import java.io.File;


public interface StorageService {

    void uploadObject(String bucketName, String modelName, File file);

    String getObkectUrl(String bucketName, String objectKey);

}
