package acceler.ocdl.controller;

import acceler.ocdl.dto.Response;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.model.Project;
import acceler.ocdl.model.StorageLoader;
import acceler.ocdl.utils.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.Map;

import static acceler.ocdl.dto.Response.getBuilder;

@Controller
@RequestMapping(path = "/rest/persistence")
public class PersistenceController {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceController.class);

    @Value("${data.path}")
    public static String defaultDataPath;

    @ResponseBody
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public final Response existPersistenceFile() {

        Response.Builder responseBuilder = getBuilder();

        // if server already start
        if (Project.getProjectInStorage() != null && Project.getDataPathInStorage() != null) {
            return responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(true).build();
        }

        // server start
        if (SerializationUtils.existDefaultSerializedFile(defaultDataPath)) {

            StorageLoader.initStorage(defaultDataPath);
            responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(true);
        } else {
            responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(false);
        }
        return responseBuilder.build();
    }


    @ResponseBody
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public final Response createPersistenceFile(@RequestBody Map<String, String> dataPath) {

        Response.Builder responseBuilder = getBuilder();

        SerializationUtils.createSerializedFile(dataPath.get("path"));
        StorageLoader.initStorage(dataPath.get("path"));

        return responseBuilder.setCode(Response.Code.SUCCESS).build();
    }


    @ResponseBody
    @RequestMapping(path = "/", method = RequestMethod.PUT)
    public final Response importPersistenceFile(@RequestBody Map<String, String> dataPath) {

        Response.Builder responseBuilder = getBuilder();
        String directory = dataPath.get("path").substring(0, dataPath.get("path").lastIndexOf("/"));

        File serializedFile = new File(directory);
        File[] serializedFiles = serializedFile.listFiles();

        // check the validation of imported file
        boolean hasProject = false;
        boolean hasUser = false;
        for (File f : serializedFiles) {
            if (hasProject == true && hasUser == true) {
                break;
            }

            if (hasProject == false && f.getName().equals("")) {
                hasProject = true;
            }
            if (hasUser == false && f.getName().equals("")) {
                hasUser = true;
            }
        }
        if (hasProject == true && hasUser == true) {
            StorageLoader.initStorage(directory);
        } else {
            throw new OcdlException("Import file error!");
        }

        return responseBuilder.setCode(Response.Code.SUCCESS).build();
    }


}
