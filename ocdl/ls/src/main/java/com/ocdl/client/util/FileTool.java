package com.ocdl.client.util;

import com.ocdl.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileTool {

    private static final Logger logger = LoggerFactory.getLogger(FileTool.class);

    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(3 * 1000);

        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            // get input stream
            inputStream = conn.getInputStream();

            //get output stream
            makeSureDir(savePath);
            File file = new File(Paths.get(savePath, fileName).toString());
            outputStream = new FileOutputStream(file);

            // write file
            transferFromIn2Out(inputStream, outputStream);
        } finally {
            if (inputStream != null) { inputStream.close(); }
            if (outputStream != null) { outputStream.close(); }
        }

        // if download file is zip file, unzip
        String fPath = Paths.get(savePath, fileName).toString();
        if (fileName.endsWith(".zip")) {
            FileTool.unZipFiles(fPath, savePath);
        }

        // delete zip file, keep the unzip file
        File zipFile = new File(fPath);
        zipFile.delete();

        logger.info(url + " download success");
    }

    private static void makeSureDir(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    private static void transferFromIn2Out(InputStream inputStream, OutputStream outputStream) throws IOException{
        byte[] buffer = new byte[1024 * 1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
    }

    public static void unZipFiles(String zipPath, String descDir) throws IOException {

        makeSureDir(descDir);

        ZipFile zip = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            zip = new ZipFile(zipPath);
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                inputStream = zip.getInputStream(entry);
                String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");

                //判断路径是否存在,不存在则创建文件路径
                makeSureDir(outPath.substring(0, outPath.lastIndexOf('/')));

                //判断文件全路径是否为文件夹,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }

                outputStream = new FileOutputStream(outPath);
                transferFromIn2Out(inputStream, outputStream);

            }

            logger.info("Unzip Finished");
        } finally {
            if (zip != null) { zip.close(); }
            if (inputStream != null) { inputStream.close(); }
            if (outputStream != null) { outputStream.close(); }
        }
    }

}
