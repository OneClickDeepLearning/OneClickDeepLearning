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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Iterator;

import static acceler.ocdl.dto.Response.getBuilder;

@Controller
@RequestMapping(path = "/data")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(acceler.ocdl.controller.AuthController.class);

    @RequestMapping("/upload")
    public Response  springUpload(@RequestParam("file") MultipartFile file)
    {
        Response.Builder respBuilder = Response.getBuilder();
        if (!file.isEmpty()) {
            try {
                // 这里只是简单例子，文件直接输出到项目路径下。
                // 实际项目中，文件需要输出到指定位置，需要在增加代码处理。
                // 还有关于文件格式限制、文件大小限制，详见：中配置。
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(file.getOriginalFilename())));
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                respBuilder.setCode(Response.Code.ERROR).setData(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                respBuilder.setCode(Response.Code.ERROR).setData(e.getMessage());
            }
            respBuilder.setCode(Response.Code.SUCCESS).build();
        } else {
            respBuilder.setCode(Response.Code.ERROR).setData("empty file");
        }

        return respBuilder.build();
    }
}

