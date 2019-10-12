package acceler.ocdl.service.impl;

import acceler.ocdl.exception.HdfsException;
import acceler.ocdl.service.HdfsService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class DefaultHdfsServiceTest {

    @Test
    public void uploadFile() {
//        DefaultHdfsService service = new DefaultHdfsService();
//        String url = service.uploadFile("test.txt");
//        System.out.println(url);
    }

    @Test
    public void listFiles() {
        listFiles(new Path("/"));
    }

    public void listFiles(Path path) {
        Configuration conf = new Configuration();
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        try {
            URI uri = new URI("hdfs://ec2-34-230-14-57.compute-1.amazonaws.com:9000");
            //returns the configured filesystem implementation.
            FileSystem hdfs = FileSystem.get(uri, conf, "hadoop");
            FileStatus[] files = hdfs.listStatus(new Path("/"));
            for (FileStatus file: files) {
                if(file.isDirectory()) {
                    System.out.println(">>>" + file.getPath());
                    listFiles(file.getPath());
                } else {
                    System.out.println(file.getPath());
                }
            }

        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new HdfsException(e.getMessage());
        }
    }

}