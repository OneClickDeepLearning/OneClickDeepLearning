package com.ocdl.proxy.exception;

public class ProxyException extends RuntimeException {

    private String errMessage;

    public ProxyException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }
}
