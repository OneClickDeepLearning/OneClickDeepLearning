package acceler.ocdl.service.impl;

import acceler.ocdl.service.HdfsService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class DefaultHdfsService implements HdfsService {

    private Configuration conf = new Configuration();
    private final String user = "hadoop";
    private FileSystem hdfs;

    public void downloadUserSpace(String srcPath, String dstPath){

        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        try {
            URI uri = new URI("hdfs://10.8.0.14:9000");
            hdfs = FileSystem.get(uri,conf,user);
            download(srcPath,dstPath);

        } catch (URISyntaxException | InterruptedException | IOException e){
            e.printStackTrace();
        }
    }



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
            hdfs.copyToLocalFile(false,new Path(srcPath),new Path(dstPath),true);
        } else {
            downloadFolder(srcPath, dstPath);
        }
    }
}
