package acceler.ocdl.service;

import acceler.ocdl.exception.HdfsException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;

public interface HdfsService {

    public void downloadUserSpace(String srcPath, String dstPath) throws HdfsException;

    public void uploadFile(String srcPath, String dstPath) throws HdfsException;

    public String uploadFile(MultipartFile file) throws HdfsException;
}
