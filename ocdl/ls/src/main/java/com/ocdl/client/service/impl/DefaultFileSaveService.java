package com.ocdl.client.service.impl;

import com.ocdl.client.service.FileSaveService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


@Service
public class DefaultFileSaveService implements FileSaveService {
    private String filePath=java.net.URLDecoder.decode(getClass().getResource("/pictures").getPath()+"/","utf-8");;

    public DefaultFileSaveService() throws UnsupportedEncodingException {
        System.out.println("Fail to initial the filePath");
    }


    public String saveFile(MultipartFile file) {
        String resultMessage = "";
        if (!file.isEmpty()) {
            try {
                System.out.println(file.getSize());
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(filePath + file.getOriginalFilename())));
                System.out.println(filePath + file.getOriginalFilename());
                out.write(file.getBytes());
                out.flush();
                out.close();
                System.out.println("finish write");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                resultMessage = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                resultMessage = e.getMessage();
            }
            resultMessage = "success";
        } else {
            resultMessage = "Empty File!";
        }
        return resultMessage;
    }

    public String getFilePath() {
        return filePath;
    }


}
