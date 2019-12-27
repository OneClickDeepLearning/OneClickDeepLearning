package com.ocdl.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;


public class VulDto {

    @JsonProperty("project_name")
    @SerializedName("project_name")
    private String projectName;

    @JsonProperty("git_url")
    @SerializedName("git_url")
    private String gitUrl;

    @JsonProperty("branch")
    @SerializedName("branch")
    private String branch;

    @JsonProperty("code_url")
    @SerializedName("code_url")
    private String codeUrl;
}
