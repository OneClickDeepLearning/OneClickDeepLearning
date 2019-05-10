package com.ocdl.client.exception;

public class LsException extends RuntimeException {

    private String frontEndErrorMsg;

    public LsException(String frontEndErrorMsg) {
        super(frontEndErrorMsg);
        this.frontEndErrorMsg = frontEndErrorMsg;
    }

    public String getFrontEndErrorMsg() {
        return frontEndErrorMsg;
    }
}
