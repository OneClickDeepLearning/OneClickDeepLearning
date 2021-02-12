package acceler.ocdl.entity;

public enum ModelStatus {

    NEW("NEW"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");

    private String status;

    ModelStatus(String status) {
        this.status = status;
    }
}
