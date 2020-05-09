package acceler.ocdl.controller;

import acceler.ocdl.dto.Response;
import acceler.ocdl.dto.UploadDto;
import acceler.ocdl.entity.RUserRole;
import acceler.ocdl.entity.Role;
import acceler.ocdl.entity.User;
import acceler.ocdl.entity.UserData;
import acceler.ocdl.exception.InvalidParamException;
import acceler.ocdl.service.KubernetesService;
import acceler.ocdl.service.UserDataService;
import acceler.ocdl.service.UserService;
import acceler.ocdl.utils.EncryptionUtil;
import acceler.ocdl.utils.SecurityUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static acceler.ocdl.dto.Response.getBuilder;

@RestController
@CrossOrigin
@RequestMapping(path = "/rest")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private KubernetesService kubernetesService;

    @Autowired
    private UserDataService userDataService;

    @RequestMapping(path = "/auth/signup", method = RequestMethod.POST)
    public Response signUp(@RequestBody User user) {

        Response.Builder respBuilder = Response.getBuilder();

        boolean valid;
        if (user.getIsInnerUser()) {
            valid = (!org.apache.commons.lang.StringUtils.isEmpty(user.getUserName()))
                    && (!org.apache.commons.lang.StringUtils.isEmpty(user.getPassword()));
            // decrypted password
            //byte[] textBytes = Base64.decodeBase64(user.getPassword());
            //String decryptedPassword = EncryptionUtil.decrypt(textBytes);
            String decryptedPassword = user.getPassword();
            user.setPassword(decryptedPassword);
        } else {
            valid = (!org.apache.commons.lang.StringUtils.isEmpty(user.getUserName()))
                    && (!org.apache.commons.lang.StringUtils.isEmpty(user.getSource()))
                    && (!org.apache.commons.lang.StringUtils.isEmpty(user.getSourceId()));
        }

        if (!valid) {
            throw new InvalidParamException("Incomplete user info.");
        }

        User newUser = userService.saveUser(user);
        String token = securityUtil.requestToken(newUser);

        Map<String, Object> result = new HashMap<>();
        result.put("userId", newUser.getId());
        result.put("token", token);
        result.put("role", newUser.getUserRoles());

        return respBuilder.setCode(Response.Code.SUCCESS)
                .setData(result).build();

    }

    @RequestMapping(path = "/auth/login", method = RequestMethod.POST, params = "pwd")
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
            User loginUser = userService.getUserByUsername(credential.account);
            String token = securityUtil.requestToken(loginUser);

            Map<String, Object> result = new HashMap<>();
            result.put("userId", loginUser.getId());
            result.put("token", token);
            result.put("role", loginUser.getUserRoles());

            respBuilder.setCode(Response.Code.SUCCESS);
            respBuilder.setData(result);
        }
        return respBuilder.build();
    }

    @RequestMapping(path = "/auth/login", method = RequestMethod.POST, params = "oauth")
    @ResponseBody
    public Response login(@RequestBody Map<String, String> param) {
        final Response.Builder respBuilder = Response.getBuilder();
        respBuilder.setCode(Response.Code.SUCCESS);
        return respBuilder.build();
    }

    @RequestMapping(path = "/auth/logout", method = RequestMethod.POST)
    @ResponseBody
    public Response logout(HttpServletRequest request) {
        final Response.Builder respBuilder = Response.getBuilder();
        User user = (User) request.getAttribute("CURRENT_USER");
        if (user != null) {
            securityUtil.releaseToken(user);
            kubernetesService.releaseDockerContainer(user);
            respBuilder.setCode(Response.Code.SUCCESS);
        } else {
            respBuilder.setCode(Response.Code.ERROR);
        }
        return respBuilder.build();
    }

    @RequestMapping(path = "/auth/me", method = RequestMethod.GET)
    @ResponseBody
    public Response me(HttpServletRequest request) {
        User user = (User) request.getAttribute("CURRENT_USER");
        final Response.Builder respBuilder = Response.getBuilder();

        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("username", user.getUserName());
        String token = securityUtil.requestToken(user);
        result.put("token", token);
        //result.put("role", innerUser.getRole());

        return respBuilder.setCode(Response.Code.SUCCESS)
                .setData(result)
                .build();
    }

    @RequestMapping(path = "/auth/key", method = RequestMethod.GET)
    @ResponseBody
    public Response key() {
        EncryptionUtil.generateKey();
        final Response.Builder respBuilder = Response.getBuilder();
        respBuilder.setCode(Response.Code.SUCCESS);
        try {
            respBuilder.setData(EncryptionUtil.getKeyString(EncryptionUtil.publicKey));
        } catch (Exception e) {
            respBuilder.setMessage("Can't get the public key from server with exception: \n" + e.getMessage());
        }
        return respBuilder.build();
    }

    public static class UserCredentials {
        public String account;
        public String password;
    }


    @RequestMapping(path = "/auth", method = RequestMethod.GET)
    @ResponseBody
    public Response checkExist(@RequestParam(name = "source_id") String sourceId) {
        Response.Builder respBuilder = Response.getBuilder();
        boolean isExist = userService.isExist(sourceId);
        return respBuilder.setCode(Response.Code.SUCCESS)
                .setData(isExist).build();
    }


    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public Response getUserByName(@RequestParam(name = "name") String name) {

        Response.Builder responseBuilder = getBuilder();

        List<User> users = userService.getAllUserByNameContaining(name);

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(users)
                .build();
    }


    @RequestMapping(path = "/role", method = RequestMethod.GET)
    public Response getRoles() {

        Response.Builder responseBuilder = getBuilder();

        List<Role> roles = userService.getAllRole();

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(roles)
                .build();
    }


    @RequestMapping(path = "/userdata/get", method = RequestMethod.POST)
    public Response getUserData(@RequestBody UserData userData,
                                   @RequestParam(value = "page", required = false, defaultValue = "0") int page ,
                                   @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        Response.Builder responseBuilder = getBuilder();

        Page<UserData> userDataPage = userDataService.getUserData(userData, page, size);

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(userDataPage)
                .build();
    }


    @RequestMapping(path = "/userdata", method = RequestMethod.POST)
    public Response uploadUserData(@RequestBody UploadDto uploadDto,
                                      HttpServletRequest request) {

        Response.Builder responseBuilder = getBuilder();

        User user = (User) request.getAttribute("CURRENT_USER");
        UserData userData = userDataService.uploadUserData(uploadDto.getSrc(), user);

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(userData)
                .build();
    }

    // TODO: file
    @RequestMapping(path = "/userdata", method = RequestMethod.GET)
    public Response downloadUserData(@RequestParam(name = "refid") String refId,
                                        HttpServletRequest request) {

        Response.Builder responseBuilder = getBuilder();

        User user = (User) request.getAttribute("CURRENT_USER");
        boolean success = userDataService.downloadUserData(refId, user);

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(success)
                .build();
    }


    @RequestMapping(path = "/userdata", method = RequestMethod.DELETE)
    public Response batchDeleteUserData(@RequestBody List<UserData> userDatas) {

        Response.Builder responseBuilder = getBuilder();

        boolean success = userDataService.batchDeleteUserData(userDatas);

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(success)
                .build();
    }

    @RequestMapping(path = "/projects", method = RequestMethod.GET)
    public Response getProjectsByUser(HttpServletRequest request) {
        Response.Builder responseBuilder = getBuilder();
        User user = (User) request.getAttribute("CURRENT_USER");

        List<RUserRole> projects = userService.getProjectsByUser(user);

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(projects)
                .build();
    }

}
