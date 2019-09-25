package acceler.ocdl.service;


import java.util.List;
import java.util.Map;

public interface TemplateService {

    Map<String,List<String>> getTemplatesList();

    List<String> getCode(String name,String type);
}
