package acceler.ocdl.controller;

import acceler.ocdl.dto.Response;
import acceler.ocdl.model.User;
import acceler.ocdl.persistence.ProjectCrud;
import acceler.ocdl.persistence.UserCrud;
import acceler.ocdl.service.KubernetesService;
import acceler.ocdl.service.UserService;
import acceler.ocdl.utils.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Iterator;

import static acceler.ocdl.dto.Response.getBuilder;

@RestController
@RequestMapping(path = "/rest/data")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(acceler.ocdl.controller.AuthController.class);
    private String filePath="";

    @RequestMapping("/upload")
    public String  springUpload(@RequestParam("file") MultipartFile file)
    {
        String result="";
        if (!file.isEmpty()) {
            try {
                System.out.println(file.getSize());
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(filePath+file.getOriginalFilename())));
                out.write(file.getBytes());
                out.flush();
                out.close();
                System.out.println("finish write");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result=e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
               result=e.getMessage();
            }
            result="File :"+file.getOriginalFilename()+" upload successful!<br/> Server Path :"+filePath;
        } else {
            result="Empty File!";
        }

        return result;
    }
}

