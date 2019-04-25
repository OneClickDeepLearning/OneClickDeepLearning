package com.ocdl.client.service.impl;

import com.ocdl.client.service.FileSaveService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


@Service
public class DefaultFileSaveService implements FileSaveService {

    @Value("${pictures.path}")
    private String filePath;

    public String saveFile(MultipartFile file) {
        String resultMessage = "";
        if (!file.isEmpty()) {
            try {
                System.out.println(file.getSize());
                BufferedOutputStream out = new BufferedOutputStream(
                        //Paths.get()
                        new FileOutputStream(new File(filePath +"/"+ file.getOriginalFilename())));
                //logger
                System.out.println(filePath + file.getOriginalFilename());
                out.write(file.getBytes());
                out.flush();
                out.close();
                //logger : file name + status
                System.out.println("finish write");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                resultMessage = e.getMessage();
            } catch (IOException e) {//child class of IO exception
                e.printStackTrace();
                resultMessage = e.getMessage();
            } //finally{close}

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
