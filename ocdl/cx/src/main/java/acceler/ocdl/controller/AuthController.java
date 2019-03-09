package acceler.ocdl.controller;

import acceler.ocdl.dto.Response;
import acceler.ocdl.model.User;
import acceler.ocdl.persistence.crud.UserCrud;
import acceler.ocdl.service.UserService;
import acceler.ocdl.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping(path = "/rest/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserCrud userCrud;

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
            User loginUser = this.userCrud.getUserByAccountAndPassword(credential.account, credential.password);
            String token = securityUtil.requestToken(loginUser);
            respBuilder.setCode(Response.Code.SUCCESS);
            respBuilder.setData(token);
        }
        return respBuilder.build();
    }

    @RequestMapping(path = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        User user = (User) request.getAttribute("CURRENT_USER");
        if (user != null){
            securityUtil.releaseToken(user);
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
