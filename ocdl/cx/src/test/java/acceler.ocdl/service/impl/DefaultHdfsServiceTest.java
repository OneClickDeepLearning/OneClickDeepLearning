package acceler.ocdl.service.impl;

import acceler.ocdl.dto.FileListVO;
import acceler.ocdl.exception.HdfsException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class DefaultHdfsServiceTest {

    @Test
    public void uploadFile() {
        DefaultHdfsService service = new DefaultHdfsService();
        //String url = service.uploadFile("test.txt");
        //System.out.println(url);
    }

    @Test
    public void listFiles() {
        List<FileListVO> fileListVO = new ArrayList<>();
        fileListVO = listFiles(new Path("/"));
        System.out.println(fileListVO);
    }

    public List<FileListVO> listFiles(Path path) throws HdfsException {
        List<FileListVO> list = new ArrayList<>();
        try {
            URI uri = new URI("hdfs://10.8.0.14:9000");
            Configuration conf = new Configuration();
            conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
            //returns the configured filesystem implementation.
            FileSystem hdfs = FileSystem.get(uri, conf, "hadoop");
            FileStatus[] files = hdfs.listStatus(path);
            for (FileStatus status: files) {
                if(status.isDirectory()) {
                    FileListVO dir = new FileListVO();
                    dir.fileName = status.getPath().getName();
                    dir.fileType = "folder";
                    List<FileListVO> children = listFiles(status.getPath());
                    dir.children = new ArrayList<>();
                    dir.children.addAll(children);
                    list.add(dir);
                } else {
                    FileListVO file = new FileListVO();
                    file.fileName = status.getPath().getName();
                    file.fileType = "file";
                    file.fileSize = status.getLen();
                    file.children = null;
                    list.add(file);
                }
            }

        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new HdfsException(e.getMessage());
        }
        return list;
    }

}