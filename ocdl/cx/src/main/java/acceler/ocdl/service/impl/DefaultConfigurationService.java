package acceler.ocdl.service.impl;

import acceler.ocdl.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


@Service
public class DefaultConfigurationService implements ConfigurationService {

/*    @Value("${project.name}")
    private String projectName;
    */
    public static final Properties p = new Properties();
    public static final String path = "/application.properties";

    public void initProperties(){
        FileInputStream fis = null;
        String configPath = null;
        try {
            configPath= java.net.URLDecoder.decode(getClass().getResource(path).getPath(),"utf-8");
            fis = new FileInputStream(configPath);
            p.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String RequestProjectName() {
        initProperties();
        return (String)p.get("project.name");
    }

    public void update(String key, String value) {
        p.setProperty(key, value);
        FileOutputStream oFile = null;
        String configPath = null;
        try {
            configPath= java.net.URLDecoder.decode(getClass().getResource(path).getPath(),"utf-8");
            oFile = new FileOutputStream(configPath);
            //将Properties中的属性列表（键和元素对）写入输出流
            p.store(oFile,"");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map RequestAllConfigurationInfo(){
        Map<String,String> result=new HashMap<>();
        result.put("projectName",(String)p.get("project.name"));
        result.put("k8url",(String)p.get("k8.url"));
        result.put("modelGitAddress",(String)p.get("model.git.address"));
        result.put("templatePath",(String)p.get("template.path"));
        return result;
    }
}
