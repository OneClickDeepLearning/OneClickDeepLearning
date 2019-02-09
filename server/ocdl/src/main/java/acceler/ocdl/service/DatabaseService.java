package acceler.ocdl.service;


import java.util.List;
import java.util.Map;

public interface DatabaseService {


    public List<String> getTemplatesList();
    public Map<String,String> getTemplates(List<String> ids);

}
