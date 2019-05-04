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

    public void createDir(String dirName){
        String user = "hadoop";
        //without this configuration, will throw exception: java.io.IOException: No FileSystem for scheme: hdfs
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        try {
            URI uri = new URI(CONSTANTS.HADOOPMASTER.ADDRESS);
            //Returns the configured filesystem implementation.
            hdfs = FileSystem.get(uri,conf,user);
            hdfs.mkdirs(new Path(CONSTANTS.HADOOPMASTER.USERDIR + dirName));

        } catch (URISyntaxException | InterruptedException | IOException e){
            throw new HdfsException(e.getMessage());
        }
    }

    /**
     * WebHDFS Create and Write to a File needs two steps
     * step one: Submit a HTTP PUT request without automatically following redirects and without sending the file data
     * the request is redirected to a datanode where the file data is to be written
     * step two:  Submit another HTTP PUT request using the URL in the Location header with the file data to be written
     * this function returns the URL in the location header
     * @param fileName the file name to be uploaded to hdfs
     * @return url of the datanode where the file data is to be written
     * @throws HdfsException
     */
    public String uploadFile(String fileName) throws HdfsException{

        String result ="http://3.92.26.165:50075/webhdfs/v1/CommonSpace/" + fileName + "?op=CREATE&user.name=hadoop&namenoderpcaddress=hadoop-master:9000&createflag=&createparent=true&overwrite=true";
        return result;

    }
}
