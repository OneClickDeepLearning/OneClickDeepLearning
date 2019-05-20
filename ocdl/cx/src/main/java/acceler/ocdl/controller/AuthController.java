package acceler.ocdl.controller;

import acceler.ocdl.dto.Response;
import acceler.ocdl.model.InnerUser;
import acceler.ocdl.service.KubernetesService;
import acceler.ocdl.service.UserService;
import acceler.ocdl.utils.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/rest/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private KubernetesService kubernetesService;

    @RequestMapping(path = "/login", method = RequestMethod.POST, params = "pwd")
    @ResponseBody
    public Response login(@RequestBody UserCredentials credential) {
        boolean success;
        if (StringUtils.isEmpty(credential.account) || StringUtils.isEmpty(credential.password)) {
            success = false;
        } else {
            success = this.userService.credentialCheck(credential);
        }

        final Response.Builder respBuilder = Response.getBuilder();
        if (!success) {
            respBuilder.setCode(Response.Code.UNAUTH);
            respBuilder.setMessage("incorrect account & password");
            return respBuilder.build();
        } else {
            InnerUser loginInnerUser = userService.getUserByUsername(credential.account);
            String token = securityUtil.requestToken(loginInnerUser);

            Map<String, Object> result = new HashMap<>();
            result.put("userId", loginInnerUser.getUserId());
            result.put("token", token);
            result.put("role", loginInnerUser.getRole());

            respBuilder.setCode(Response.Code.SUCCESS);
            respBuilder.setData(result);
        }
        return respBuilder.build();
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST, params = "oauth")
    @ResponseBody
    public Response login(@RequestBody Map<String, String> param) {
        final Response.Builder respBuilder = Response.getBuilder();
        respBuilder.setCode(Response.Code.SUCCESS);
        return respBuilder.build();
    }

    @RequestMapping(path = "/logout", method= RequestMethod.POST)
    @ResponseBody
    public Response logout(HttpServletRequest request, HttpServletResponse response) {
        final Response.Builder respBuilder = Response.getBuilder();
        InnerUser innerUser = (InnerUser) request.getAttribute("CURRENT_USER");
        if (innerUser != null) {
            securityUtil.releaseToken(innerUser);
            kubernetesService.releaseDockerContainer(innerUser);
            respBuilder.setCode(Response.Code.SUCCESS);
        } else {
            respBuilder.setCode(Response.Code.ERROR);
        }
        return respBuilder.build();
    }

    @RequestMapping(path = "/me", method = RequestMethod.GET)
    @ResponseBody
    public Response me(HttpServletRequest request, @QueryParam("token")String token) {
        InnerUser innerUser = (InnerUser) request.getAttribute("CURRENT_USER");
        final Response.Builder respBuilder = Response.getBuilder();
        Map<String, Object> result = new HashMap<>();
        result.put("userId", innerUser.getUserId());
        result.put("username",innerUser.getUserName());
        result.put("token", token);
        result.put("role", innerUser.getRole());
        respBuilder.setData(result);
        respBuilder.setCode(Response.Code.SUCCESS);
        return respBuilder.build();
    }

    public static class UserCredentials {
        public String account;
        public String password;
    }
}
