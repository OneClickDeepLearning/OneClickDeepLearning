package acceler.ocdl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public final class PingController {

    @RequestMapping(path = "ping")
    public final String ping() {
        return "success!";
    }
}
