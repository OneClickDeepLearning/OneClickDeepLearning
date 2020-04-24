package acceler.ocdl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VulDataDto {

    @JsonProperty("ocdl_project_refid")
    private String projectRefId;

    @JsonProperty("project_name")
    private String projectName;

    @JsonProperty("git_url")
    private String gitUrl;

    @JsonProperty("branch")
    private String branch;

    @JsonProperty("code_url")
    private String codeUrl;

    @JsonProperty("model_path")
    private String modelPath;

    @JsonProperty("data")
    private Object data;

    @JsonProperty("create_at")
    private String createAt;
}
