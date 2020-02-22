package acceler.ocdl.dto;


import acceler.ocdl.entity.TemplateCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadDto {

    private String src;

    private TemplateCategory category;
}
