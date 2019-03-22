package acceler.ocdl.model;

import java.sql.Blob;

public class Template {

    //column: id, name, file, model_suffix, desp
    private long templateId;
    private String templateName;
    private byte[] file;
    private String suffix;
    private String desp;

    public Template() {}

    public Template(String templateName, byte[] file, String suffix, String desp) {
        this.templateName = templateName;
        this.file = file;
        this.suffix = suffix;
        this.desp = desp;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }
}
