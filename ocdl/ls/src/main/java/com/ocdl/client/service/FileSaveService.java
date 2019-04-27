package com.ocdl.client.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileSaveService {
    String saveFile(MultipartFile file);
    String getFilePath();
}
