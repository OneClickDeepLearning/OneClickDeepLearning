package acceler.ocdl.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class DefaultHdfsServiceTest {


    @Autowired
    private DefaultHdfsService service;

    @Test
    public void uploadFile() {

        String url = service.uploadFile("test.txt");
        System.out.println(url);
    }
}