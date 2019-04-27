package com.ocdl.client.service;

import java.io.IOException;

public interface ConfigurationService {
    String getProjectName();
    Boolean setProjectName(String projectName);
}
