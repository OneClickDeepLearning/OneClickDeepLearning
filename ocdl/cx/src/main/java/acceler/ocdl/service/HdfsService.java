package acceler.ocdl.service;

import acceler.ocdl.exception.HdfsException;
import org.apache.hadoop.fs.Path;

public interface HdfsService {

    public void uploadFile(Path srcPath, Path destPath) throws HdfsException;

    public void createDir(Path dirPath) throws HdfsException;
}
