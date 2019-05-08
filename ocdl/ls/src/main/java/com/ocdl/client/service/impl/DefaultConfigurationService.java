//package com.ocdl.client.service.impl;
//
//import com.ocdl.client.service.ConfigurationService;
//import com.ocdl.client.util.SpringContextUtil;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@Service
//public class DefaultConfigurationService implements ConfigurationService {
//
//    public String getProjectName() {
//        SpringContextUtil.updateProp(getClass().getResource("/application.properties").getPath());
//        String projectName = SpringContextUtil.getPropertiesValue("project.name");
//        return projectName;
//    }
//
//    @Override
//    public Boolean setProjectName(String projectName){
//        SpringContextUtil.updateProp(getClass().getResource("/application.properties").getPath());
//        return SpringContextUtil.changeProp("project.name",projectName);
//    }
//
//}
