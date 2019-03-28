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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static acceler.ocdl.dto.Response.getBuilder;

@Controller
@RequestMapping(path = "/rest/data")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(acceler.ocdl.controller.AuthController.class);
    private String filePath="D:/springUpload";

    @RequestMapping("/upload")
    public Response  springUpload(HttpServletRequest request)
    {
        Response.Builder responseBuilder = getBuilder();
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
            //获取multiRequest 中所有的文件名
            Iterator iter=multiRequest.getFileNames();

            while(iter.hasNext())
            {
                //一次遍历所有文件
                MultipartFile file=multiRequest.getFile(iter.next().toString());
                if(file!=null)
                {
                    String path=filePath+file.getOriginalFilename();
                    //上传
                    try {
                        file.transferTo(new File(path));
                    } catch (IOException e) {
                        e.printStackTrace();
                        responseBuilder.setCode(Response.Code.ERROR)
                                .setData(e);
                        return responseBuilder.build();
                    }
                }
            }
            responseBuilder.setCode(Response.Code.SUCCESS);
        }else{
            responseBuilder.setCode(Response.Code.ERROR);
        }

        return responseBuilder.build();
    }
}

