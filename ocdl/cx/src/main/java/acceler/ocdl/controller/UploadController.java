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
import java.io.*;
import java.util.Iterator;
import java.util.Map;

import static acceler.ocdl.dto.Response.getBuilder;

@RestController
@RequestMapping(path = "/rest/data")
public class UploadController {


    @Autowired
    private HdfsService hdfsService;

    private static final Logger logger = LoggerFactory.getLogger(acceler.ocdl.controller.AuthController.class);
    private String filePath = "";

    @RequestMapping(path = "/upload", method = RequestMethod.PUT)
    public Response springUpload(@RequestBody Map<String, String> param, HttpServletRequest request) {
        String fileName = param.get("filename");

        String url = hdfsService.uploadFile(fileName);

        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .setData(url)
                .build();
    }
}

