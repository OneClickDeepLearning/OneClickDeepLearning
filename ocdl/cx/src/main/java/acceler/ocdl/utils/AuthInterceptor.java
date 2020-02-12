package acceler.ocdl.utils;

import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.User;
import acceler.ocdl.service.ProjectService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Order(1)
@Component
public final class AuthInterceptor extends HandlerInterceptorAdapter {
    private static final String AUTH_TOKEN_HEADER = "AUTH_TOKEN";
    private static final String PROJECT_HEADER = "PROJECT";

    @Resource
    private SecurityUtil securityUtil;

    @Autowired
    private ProjectService projectService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader(AUTH_TOKEN_HEADER);
        if (StringUtils.isEmpty(token)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        User accessUser = securityUtil.getUserByToken(token);
        if (null == accessUser) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        // if header has project
        String projectRefId = request.getHeader(PROJECT_HEADER);
        if (!StringUtils.isEmpty(projectRefId)) {

            Project project = projectService.getProject(projectRefId);
            // verify if project exist
            if (null == project) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }

            // verify if project belongs to the accessUser
            if (project.getUserRoles().stream()
                    .noneMatch(u -> u.getUser().getId().equals(accessUser.getId()))) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
            request.setAttribute("PROJECT", project);
        }


        request.setAttribute("CURRENT_USER", accessUser);
        request.setAttribute("CURRENT_TOKEN", token);
        return true;
    }
}
