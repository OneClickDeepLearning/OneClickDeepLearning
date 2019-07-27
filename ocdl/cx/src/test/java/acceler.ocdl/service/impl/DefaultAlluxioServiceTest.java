//package acceler.ocdl.service.impl;
//
//import alluxio.AlluxioURI;
//import alluxio.client.file.FileInStream;
//import alluxio.client.file.FileSystem;
//import alluxio.exception.AlluxioException;
//import org.apache.commons.io.IOUtils;
//import org.junit.Test;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class DefaultAlluxioServiceTest {
//
//    @Test
//    public void testCreatDir() throws Exception{
//        FileSystem fs = FileSystem.Factory.get();
//        AlluxioURI path = new AlluxioURI("/NewDir");
//
//        fs.createFile(path);
//
//    }
//
//    @Test
//    public void testDownloadFile() throws Exception{
//        FileSystem fs = FileSystem.Factory.get();
//        AlluxioURI path = new AlluxioURI("/UserSpace/1-3/unet_membrane.hdf5");
//
//        FileInStream in = fs.openFile(path);
//        FileOutputStream fileOutputStream = new FileOutputStream("/root/unet_membrane.hdf5");
//        fileOutputStream.write(IOUtils.toByteArray(in));
//        in.close();
//        fileOutputStream.close();
//    }
//}
