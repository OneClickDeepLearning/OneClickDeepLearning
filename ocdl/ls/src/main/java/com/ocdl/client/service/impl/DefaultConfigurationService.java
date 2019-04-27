package com.ocdl.client.service.impl;

import com.ocdl.client.util.SpringContextUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DefaultConfigurationService {

    public String saveFile(MultipartFile file) {
        String projectName = SpringContextUtil.getPropertiesValue("project.name");
        return projectName;
    }

}
