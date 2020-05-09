package acceler.ocdl.dto;

import org.codehaus.jackson.annotate.JsonProperty;
import java.util.List;

public class FileListVO {

    @JsonProperty("file_name")
    public String fileName;

    @JsonProperty("file_type")
    public String fileType;

    @JsonProperty("file_size")
    public Long fileSize;

    @JsonProperty("children")
    public List<FileListVO> children;

}
