package acceler.ocdl.service;

import acceler.ocdl.exception.HdfsException;
import org.apache.hadoop.fs.Path;

public interface HdfsService {

    public String uploadFile(String fileName) throws HdfsException;

    void createDir(Path dirPath);
}
