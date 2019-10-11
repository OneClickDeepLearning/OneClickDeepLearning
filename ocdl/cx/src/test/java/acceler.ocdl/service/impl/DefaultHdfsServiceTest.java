package acceler.ocdl.service.impl;

import acceler.ocdl.service.HdfsService;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class DefaultHdfsServiceTest {

    @Autowired HdfsService hdfsService;

    @Test
    public void uploadFile() {
//        DefaultHdfsService service = new DefaultHdfsService();
//        String url = service.uploadFile("test.txt");
//        System.out.println(url);
    }

    @Test
    public void listFiles() {
        Path path = new Path("/");
        hdfsService.listFiles(path);
    }

}