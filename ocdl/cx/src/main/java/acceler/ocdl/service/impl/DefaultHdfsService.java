package acceler.ocdl.service.impl;

import acceler.ocdl.dto.FileListVO;
import acceler.ocdl.exception.HdfsException;
import acceler.ocdl.service.HdfsService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultHdfsService implements HdfsService {

    private Configuration conf;

    @Value("${HDFS.IP_ADDRESS}")
    private String hdfsIpAddress;
    @Value("${HDFS.USER_NAME}")
    private String hdfsUserName;

    public DefaultHdfsService() {
        conf = new Configuration();
        //without this configuration, will throw exception: java.io.IOException: No FileSystem for scheme: hdfs
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
    }

    /**
     * List all files on HDFS certain path
     * @param path the path on HDFS
     * @throws HdfsException
     */
    @Override
    public List<FileListVO> listFiles(Path path) throws HdfsException {
        List<FileListVO> list = new ArrayList<>();
        try {
            URI uri = new URI(hdfsIpAddress);
            //returns the configured filesystem implementation.
            FileSystem hdfs = FileSystem.get(uri, conf, hdfsUserName);
            FileStatus[] files = hdfs.listStatus(path);
            for (FileStatus status: files) {
                if(status.isDirectory()) {
                    FileListVO dir = new FileListVO();
                    dir.fileName = status.getPath().getName();
                    dir.fileType = "folder";
                    List<FileListVO> children = listFiles(status.getPath());
                    dir.children = new ArrayList<>();
                    dir.children.addAll(children);
                    list.add(dir);
                } else {
                    FileListVO file = new FileListVO();
                    file.fileName = status.getPath().getName();
                    file.fileType = "file";
                    file.fileSize = status.getBlockSize();
                    file.children = null;
                    list.add(file);
                }
            }
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new HdfsException(e.getMessage());
        }
        return list;
    }

    /**
     * Create an empty directory on HDFS
     * @param dirPath the taget directory path on HDFS
     * @throws HdfsException
     */
    public void createDir(Path dirPath) throws HdfsException{
        try {
            URI uri = new URI(hdfsIpAddress);
            //returns the configured filesystem implementation.
            FileSystem hdfs = FileSystem.get(uri, conf, hdfsUserName);
            hdfs.mkdirs(dirPath);
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new HdfsException(e.getMessage());
        }
    }

    /**
     * Upload an existing local file to HDFS
     * WebHDFS Create and Write to a File needs two steps
     * step one: Submit a HTTP PUT request without automatically following redirects and without sending the file data
     * the request is redirected to a datanode where the file data is to be written
     * step two:  Submit another HTTP PUT request using the URL in the Location header with the file data to be written
     * this function returns the URL in the location header
     *
     * @param srcPath the source file to be uploaded to HDFS
     * @param destPath the destination path on HDFS
     * @throws HdfsException
     */
    public void uploadFile(Path srcPath, Path destPath) throws HdfsException {
        //String result = "http://3.92.26.165:50075/webhdfs/v1/CommonSpace/" + fileName + "?op=CREATE&user.name=hadoop&namenoderpcaddress=hadoop-master:9000&createflag=&createparent=true&overwrite=true";
        try {
            URI uri = new URI(hdfsIpAddress);
            FileSystem hdfs = FileSystem.get(uri, conf, hdfsUserName);
            hdfs.copyFromLocalFile(true,true,srcPath,destPath);
        } catch (URISyntaxException | InterruptedException | IOException e){
            throw new HdfsException(e.getMessage());
        }
    }

    public void downloadFile(Path srcPath, Path destPath) throws HdfsException {
        try {
            URI uri = new URI(hdfsIpAddress);
            FileSystem hdfs = FileSystem.get(uri, conf, hdfsUserName);
            hdfs.copyToLocalFile(false,srcPath,destPath);
        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new HdfsException(e.getMessage());
        }
    }
}
