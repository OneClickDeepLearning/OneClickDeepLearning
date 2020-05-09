package acceler.ocdl.service.impl;

import acceler.ocdl.exception.AlluxioException;
import acceler.ocdl.service.AlluxioService;
import alluxio.AlluxioURI;
import alluxio.client.file.FileInStream;
import alluxio.client.file.FileSystem;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class DefaultAlluxioService implements AlluxioService {

    @Value("${APPLICATIONS_DIR.USER_SPACE}")
    String localUserDir;

    @Override
    public void downloadFromStaging(String fileName, Long userId) throws AlluxioException {
        FileSystem fs = FileSystem.Factory.get();
        AlluxioURI path = new AlluxioURI("/staging/" + fileName);
        System.out.println(path.toString());

        try {
            FileInStream in = fs.openFile(path);
            FileOutputStream fileOutputStream = new FileOutputStream(localUserDir + userId.toString() + "/" + fileName);
            fileOutputStream.write(IOUtils.toByteArray(in));
            in.close();
            fileOutputStream.close();
        } catch (alluxio.exception.AlluxioException | IOException e){
            throw new AlluxioException(e.getMessage());
        }
    }
}
