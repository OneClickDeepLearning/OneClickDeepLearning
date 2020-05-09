package acceler.ocdl.utils;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.User;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Order(3)
@Component
public class ManagerAuthInterceptor extends HandlerInterceptorAdapter {
    static final String[] INTERCEPTED_URLS = {
            "/rest/model/**"
    };

    static final String[] EXCEPTED_URLS = {
            "/rest/model/event",
            "/rest/model/init"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        User accessUser = (User) request.getAttribute("CURRENT_USER");
        Project project = (Project) request.getAttribute("PROJECT");

        if (accessUser.getUserRoles().stream()
                .filter(rUserRole -> rUserRole.getProject().getRefId().equals(project.getRefId()))
                .noneMatch(rUserRole -> rUserRole.getRole().getName().equals(CONSTANTS.ROLE_TABLE.ROLE_MAN))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }
}
