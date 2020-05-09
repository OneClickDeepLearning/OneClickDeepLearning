package acceler.ocdl.utils;

import acceler.ocdl.entity.User;
import org.apache.commons.lang.StringUtils;
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

    @Resource
    private SecurityUtil securityUtil;

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

        request.setAttribute("CURRENT_USER", accessUser);
        request.setAttribute("CURRENT_TOKEN", token);
        return true;
    }
}
