package acceler.ocdl.controller;

import acceler.ocdl.dto.FileListVO;
import acceler.ocdl.dto.Response;
import acceler.ocdl.entity.User;
import acceler.ocdl.service.HdfsService;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@CrossOrigin
@RequestMapping(path = "/rest/data")
public class FileController {

    @Autowired
    private HdfsService hdfsService;

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @RequestMapping(path="/upload", method = RequestMethod.PUT)
    @ResponseBody
    public Response springUpload(@RequestBody Map<String,String> file) {
        //TODO: add a new function for upload data -> Boqian

        Response.Builder builder = Response.getBuilder();
//        if(!file.isEmpty()){
//            String result =  hdfsService.uploadFile(file.get("file"));
//            builder.setCode(Response.Code.SUCCESS).setData(result);
//
//        } else {
//            builder.setCode(Response.Code.ERROR).setMessage("Empty file!");
//        }
        return builder.build();
    }

    @RequestMapping(path="/list", method = RequestMethod.GET)
    @ResponseBody
    public final Response hdfsList(HttpServletRequest request) {
        User user = (User) request.getAttribute("CURRENT_USER");
        Path path = new Path("/UserSpace/" + user.getId().toString());
        List<FileListVO> list = new ArrayList<>();
        list = hdfsService.listFiles(path);
        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .setData(list)
                .build();
    }
}

