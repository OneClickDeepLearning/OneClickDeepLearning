package acceler.ocdl.utils;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = OcdlException.class)
    public Response exceptionsHandling(OcdlException exception) {
        String frontErrorMsg= exception.getFrontEndErrorMsg();

        return Response.getBuilder()
                .setCode(Response.Code.ERROR)
                .setMessage(frontErrorMsg)
                .build();
    }
}
