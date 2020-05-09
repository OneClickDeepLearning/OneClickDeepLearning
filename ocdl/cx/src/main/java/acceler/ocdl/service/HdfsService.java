package acceler.ocdl.service;

import acceler.ocdl.dto.FileListVO;
import acceler.ocdl.exception.HdfsException;
import org.apache.hadoop.fs.Path;
import java.util.List;

public interface HdfsService {

    public List<FileListVO> listFiles(Path path) throws HdfsException;

    public void uploadFile(Path srcPath, Path destPath) throws HdfsException;

    public void downloadFile(Path srcPath, Path destPath) throws HdfsException;

    public void createDir(Path dirPath) throws HdfsException;
}
