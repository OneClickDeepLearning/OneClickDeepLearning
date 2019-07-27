package acceler.ocdl.utils;

import acceler.ocdl.model.InnerUser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Order(2)
@Component
public class ManagerAuthInterceptor extends HandlerInterceptorAdapter {
    static final String[] INTERCEPTED_URLS = {
            "/rest/auth"
    };

    static final String[] EXCEPTED_URLS = {
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        InnerUser accessInnerUser = (InnerUser) request.getAttribute("CURRENT_USER");
        return accessInnerUser.getRole() == InnerUser.Role.MANAGER;
    }
}
