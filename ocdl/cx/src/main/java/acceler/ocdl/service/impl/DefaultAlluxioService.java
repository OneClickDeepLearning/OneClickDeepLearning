package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.exception.AlluxioException;
import acceler.ocdl.service.AlluxioService;
import alluxio.AlluxioURI;
import alluxio.client.file.FileInStream;
import alluxio.client.file.FileSystem;
import alluxio.exception.FileDoesNotExistException;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DefaultAlluxioService implements AlluxioService {

    @Override
    public void downloadFromStaging(String fileName, Long userId) throws AlluxioException {
        FileSystem fs = FileSystem.Factory.get();
        AlluxioURI path = new AlluxioURI("/Staging/" + fileName);

        try {
            FileInStream in = fs.openFile(path);
            FileOutputStream fileOutputStream = new FileOutputStream(CONSTANTS.APPLICATIONS_DIR.USER_SPACE + userId.toString() + "/" + fileName);
            fileOutputStream.write(IOUtils.toByteArray(in));
            in.close();
            fileOutputStream.close();
        } catch (alluxio.exception.AlluxioException | IOException e){
            throw new AlluxioException(e.getMessage());
        }
    }
}
