package acceler.ocdl.service;


import java.util.List;
import java.util.Map;

public interface TemplateService {

    public List<String> getTemplatesList(String type);
//    public Map<String,String> getTemplates(List<String> ids);
    public List<String> getTemplates2(String name,String type);
}
