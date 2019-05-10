package com.ocdl.client.util;

import com.ocdl.client.exception.LsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = LsException.class)
    public Response exceptionsHandling(LsException exception) {
        String frontErrorMsg= exception.getFrontEndErrorMsg();

        return Response.getBuilder()
                .setCode(Response.Code.ERROR)
                .setMessage(frontErrorMsg)
                .build();
    }
}
