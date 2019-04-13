package acceler.ocdl.service.impl;

import acceler.ocdl.exception.HdfsException;
import acceler.ocdl.service.HdfsService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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

    private static final Map<String,String> ipMap = new HashMap<String, String>(){
        {
            put("hadoop-slave-1", "3.92.26.165");
            put("hadoop-slave-2", "34.229.23.136");
        }
    };

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
    public String uploadFile(MultipartFile file) throws HdfsException{

        StringBuilder url = new StringBuilder();
        String fileName = file.getName();
        url.append("http://52.203.173.33:50070/webhdfs/v1/CommonSpace/").append(fileName).append("?op=CREATE&user.name=hadoop&overwrite=true");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String dataNodeUrl = "";

        ResponseEntity<String> responseEntity = restTemplate.exchange(url.toString(), HttpMethod.PUT,entity,String.class);
        try {
             dataNodeUrl = responseEntity.getHeaders().get("Location").get(0);
        } catch (NullPointerException e){
            throw new HdfsException("Location value is null in response entity from uploadFile method");
        }

        if(responseEntity.getStatusCode() != HttpStatus.TEMPORARY_REDIRECT){
            return "Uploading failed";
        }else {
            for(String nodeName : ipMap.keySet()){
                if(dataNodeUrl.contains(nodeName)){
                    dataNodeUrl.replaceAll(nodeName,ipMap.get(nodeName));
                    break;
                }
            }

        }
        return dataNodeUrl;

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
