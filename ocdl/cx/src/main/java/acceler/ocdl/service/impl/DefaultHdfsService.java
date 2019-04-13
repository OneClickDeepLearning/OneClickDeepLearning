package acceler.ocdl.service.impl;

import acceler.ocdl.exception.HdfsException;
import acceler.ocdl.service.HdfsService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@Service
public class DefaultHdfsService implements HdfsService {

    private Configuration conf = new Configuration();

    private FileSystem hdfs;

    public void downloadUserSpace(String srcPath, String dstPath) throws HdfsException{
        String user = "hadoop";
        //without this configuration, will throw exception: java.io.IOException: No FileSystem for scheme: hdfs
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        try {
            URI uri = new URI("hdfs://10.8.0.14:9000");
            //Returns the configured filesystem implementation.
            hdfs = FileSystem.get(uri,conf,user);
            download(srcPath,dstPath);

        } catch (URISyntaxException | InterruptedException | IOException e){
            throw new HdfsException(e.getMessage());
        }
    }

    public void uploadFile (String srcPath, String dstPath) throws HdfsException{
        String user = "hadoop";
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        try {
            URI uri = new URI("hdfs://10.8.0.14:9000");
            hdfs = FileSystem.get(uri,conf,user);

            //upload file to hdfs, do not delete src file, do not overwrite
            hdfs.copyFromLocalFile(false,false,new Path(srcPath),new Path(dstPath));

        } catch (URISyntaxException | InterruptedException | IOException e){
            throw new HdfsException(e.getMessage());
        }
    }

    public String uploadFile(String fileName) throws HdfsException{

        StringBuilder url = new StringBuilder();
        url.append("http://52.203.173.33:50070/webhdfs/v1/CommonSpace/").append(fileName).append("?op=CREATE&user.name=hadoop");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url.toString(), HttpMethod.PUT,entity,String.class);
        try {
            return responseEntity.getHeaders().get("Location").get(0);
        } catch (NullPointerException e){
            throw new HdfsException("Location value is null in response entity from uploadFile method");
        }
    }

    //Download the whole folder from hdfs
    private void downloadFolder(String srcPath, String dstPath) throws IOException {
        File dstDir = new File(dstPath);
        if (!dstDir.exists()) {
            dstDir.mkdirs();
        }
        FileStatus[] srcFileStatus = hdfs.listStatus(new Path(srcPath));
        Path[] srcFilePath = FileUtil.stat2Paths(srcFileStatus);
        for (int i = 0; i < srcFilePath.length; i++) {
            String srcFile = srcFilePath[i].toString();
            int fileNamePosi = srcFile.lastIndexOf('/');
            String fileName = srcFile.substring(fileNamePosi + 1);
            download(srcPath + '/' + fileName, dstPath + '/' + fileName);
        }
    }

    private void download(String srcPath, String dstPath) throws IOException{
        if (hdfs.isFile(new Path(srcPath))) {
            //download file from hdfs, do not delete src file
            hdfs.copyToLocalFile(false,new Path(srcPath),new Path(dstPath),true);
        } else {
            downloadFolder(srcPath, dstPath);
        }
    }
}
