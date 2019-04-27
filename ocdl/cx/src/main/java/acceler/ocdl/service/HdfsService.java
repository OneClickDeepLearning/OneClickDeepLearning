package acceler.ocdl.service;

import acceler.ocdl.exception.HdfsException;

public interface HdfsService {

    //public void downloadUserSpace(String srcPath, String dstPath) throws HdfsException;

    //public void uploadFile(String srcPath, String dstPath) throws HdfsException;

    public void createUserSpace(String userSpace);

}
