package acceler.ocdl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/ping")
public final class PingController {

    @RequestMapping(method = RequestMethod.GET)
    public final String ping() {
        return "success!";
    }
}
