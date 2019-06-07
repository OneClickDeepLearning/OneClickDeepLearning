package com.ocdl.client.service;

import java.io.File;


public interface StorageService {

    String uploadObjectAndGetUrl(String bucketName, String fileName, File file);
}
