package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.exception.HdfsException;
import acceler.ocdl.service.HdfsService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class DefaultHdfsService implements HdfsService {

    private Configuration conf;

    public DefaultHdfsService() {
        conf = new Configuration();
        //without this configuration, will throw exception: java.io.IOException: No FileSystem for scheme: hdfs
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
    }

    public void createDir(Path dirPath) {
        try {
            URI uri = new URI(CONSTANTS.HDFS.IP_ADDRESS);
            //returns the configured filesystem implementation.
            FileSystem hdfs = FileSystem.get(uri, conf, CONSTANTS.HDFS.USER_NAME);
            hdfs.mkdirs(dirPath);
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new HdfsException(e.getMessage());
        }
    }
}
