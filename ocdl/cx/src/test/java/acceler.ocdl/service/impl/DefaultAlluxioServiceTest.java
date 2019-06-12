package acceler.ocdl.service.impl;

import alluxio.AlluxioURI;
import alluxio.client.file.FileSystem;
import alluxio.exception.AlluxioException;
import org.junit.Test;

import java.io.IOException;

public class DefaultAlluxioServiceTest {

    @Test
    public void testCreatDir(){
        FileSystem fs = FileSystem.Factory.get();
        AlluxioURI path = new AlluxioURI("/NewDir");
        try {
            fs.createFile(path);
        } catch (AlluxioException | IOException e){
            e.printStackTrace();
        }
    }
}
