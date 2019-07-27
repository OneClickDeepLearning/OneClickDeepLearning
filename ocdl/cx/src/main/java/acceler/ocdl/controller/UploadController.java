package acceler.ocdl.controller;

import acceler.ocdl.dto.Response;
import acceler.ocdl.service.HdfsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping(path = "/rest/data")
public class UploadController {


    @Autowired
    private HdfsService hdfsService;

    private static final Logger logger = LoggerFactory.getLogger(acceler.ocdl.controller.UploadController.class);

    @RequestMapping(path="/upload", method = RequestMethod.PUT)
    @ResponseBody
    public Response springUpload(@RequestBody Map<String,String> file) {
        //TODO: add a new function for upload data -> Boqian

/*        Response.Builder builder = Response.getBuilder();

        if(!file.isEmpty()){

            String result =  hdfsService.uploadFile(file.get("file"));

            builder.setCode(Response.Code.SUCCESS).setData(result);

        } else {
            builder.setCode(Response.Code.ERROR).setMessage("Empty file!");
        }

        return builder.build();
        */
    return null;
    }
}

