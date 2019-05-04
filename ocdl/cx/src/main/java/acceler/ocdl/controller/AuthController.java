package acceler.ocdl.controller;

import acceler.ocdl.dto.Response;
import acceler.ocdl.model.InnerUser;
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

    @RequestMapping(path = "/login", method = RequestMethod.POST)
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
            InnerUser loginInnerUser = this.userCrud.getUserByAccountAndPassword(credential.account, credential.password);
            String token = securityUtil.requestToken(loginInnerUser);

            Map<String, Object> result = new HashMap<>();
            result.put("userName", loginInnerUser.getAuthServerUserId());
            result.put("token", token);
            result.put("role", loginInnerUser.getRole());

            respBuilder.setCode(Response.Code.SUCCESS);
            respBuilder.setData(result);
        }
        return respBuilder.build();
    }

    @RequestMapping(path = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        InnerUser innerUser = (InnerUser) request.getAttribute("CURRENT_USER");
        if (innerUser != null){
            securityUtil.releaseToken(innerUser);
            response.setStatus(HttpServletResponse.SC_OK);
        }else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public static class UserCredentials {
        public String account;
        public String password;
    }
}
