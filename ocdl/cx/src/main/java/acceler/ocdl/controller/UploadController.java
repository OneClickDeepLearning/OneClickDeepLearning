package acceler.ocdl.controller;

import acceler.ocdl.dto.Response;
import acceler.ocdl.model.User;
import acceler.ocdl.persistence.ProjectCrud;
import acceler.ocdl.persistence.UserCrud;
import acceler.ocdl.service.HdfsService;
import acceler.ocdl.service.KubernetesService;
import acceler.ocdl.service.UserService;
import acceler.ocdl.utils.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static acceler.ocdl.dto.Response.getBuilder;

@Controller
@RequestMapping(path = "/rest/data")
public class UploadController {


    @Autowired
    private HdfsService hdfsService;

    private static final Logger logger = LoggerFactory.getLogger(acceler.ocdl.controller.AuthController.class);

    @RequestMapping(path="/upload", method = RequestMethod.POST)
    public Response  springUpload(@RequestParam("file") MultipartFile file)
    {

        if(!file.isEmpty()){
            String fileName = file.getName();

//                BufferedOutputStream out = new BufferedOutputStream(
//                        new FileOutputStream(new File(filePath+file.getOriginalFilename())));
//                out.write(file.getBytes());


            String result =  hdfsService.uploadFile(fileName,file);

//                out.flush();
//                out.close();

            if (result.equals("success"))

                return Response.getBuilder()
                    .setCode(Response.Code.SUCCESS)
                    .setData(result)
                    .build();

            else

                return Response.getBuilder()
                        .setCode(Response.Code.ERROR)
                        .setData(result)
                        .build();



        } else {
            return Response.getBuilder()
                    .setCode(Response.Code.ERROR)
                    .setData("Empty file!")
                    .build();
        }

    }
}

