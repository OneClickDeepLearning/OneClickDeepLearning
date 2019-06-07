package acceler.ocdl.service;


import java.util.List;

public interface TemplateService {

    List<String> getTemplatesList(String type);

    List<String> getTemplates2(String name,String type);
}
