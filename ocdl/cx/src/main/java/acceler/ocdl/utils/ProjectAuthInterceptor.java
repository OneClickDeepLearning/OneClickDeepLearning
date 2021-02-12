package acceler.ocdl.utils;

import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.User;
import acceler.ocdl.service.ProjectService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Order(2)
@Component
public class ProjectAuthInterceptor extends HandlerInterceptorAdapter {

    private static final String PROJECT_HEADER = "PROJECT";

    @Autowired
    private ProjectService projectService;

    static final String[] INTERCEPTED_URLS = {
            "/rest/model/**",
            "/rest/project/algorithm/**",
            "/rest/project/suffix/**",
            "/rest/project/projectdata/**",
            "/rest/project/coop",
            "/rest/template/**"
    };

    static final String[] EXCEPTED_URLS = {
            "/rest/project/projectdata/recycle"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        User accessUser = (User) request.getAttribute("CURRENT_USER");
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
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }
}
