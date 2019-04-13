package acceler.ocdl.model;

import java.io.Serializable;

public class Template implements Serializable {
    private static final long serialVersionUID = -2767605614048989439L;

    private long templateId;
    private String templateName;
    private byte[] file;
    private String suffix;
    private String description;

    public Template() {
    }

    public Template(String templateName, byte[] file, String suffix, String description) {
        this.templateName = templateName;
        this.file = file;
        this.suffix = suffix;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
