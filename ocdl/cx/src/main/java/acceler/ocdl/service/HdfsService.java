package acceler.ocdl.service;

import org.apache.hadoop.fs.Path;

public interface HdfsService {

    void createDir(Path dirPath);

}
