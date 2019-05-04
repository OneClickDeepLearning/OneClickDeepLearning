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

    private Configuration conf = new Configuration();

    private FileSystem hdfs;

    public void createDir(String userSpace){
        String user = "hadoop";
        //without this configuration, will throw exception: java.io.IOException: No FileSystem for scheme: hdfs
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        try {
            URI uri = new URI(CONSTANTS.HADOOPMASTER.ADDRESS);
            //Returns the configured filesystem implementation.
            hdfs = FileSystem.get(uri,conf,user);
            hdfs.mkdirs(new Path(CONSTANTS.HADOOPMASTER.USERDIR + userSpace));

        } catch (URISyntaxException | InterruptedException | IOException e){
            throw new HdfsException(e.getMessage());
        }
    }
}
