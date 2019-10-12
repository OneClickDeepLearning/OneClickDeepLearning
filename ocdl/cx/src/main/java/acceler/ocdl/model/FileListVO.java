package acceler.ocdl.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class FileListVO {

    @JsonProperty("file_name")
    public String fileName;

    @JsonProperty("file_type")
    public String fileType;

    @JsonProperty("children")
    public List<FileListVO> children;

}
