package acceler.ocdl.service;

import java.util.Map;

public interface ConfigurationService {
    public String RequestProjectName();
    public Map RequestAllConfigurationInfo();
    public void update(String key, String value);
}
