package com.ocdl.client.service;

import java.io.File;


public interface StorageService {

    public void createStorage();

    public void uploadObject(String bucketName, String modelName, File file);

    public String getObkectUrl(String bucketName, String objectKey);
}
