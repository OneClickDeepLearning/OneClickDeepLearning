package acceler.ocdl.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
@Configuration
public class InterceptorUtil extends WebMvcConfigurerAdapter {

    @Resource
    private AuthInterceptor authInterceptor;

    @Resource
    private ManagerAuthInterceptor managerAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/rest/**").excludePathPatterns("/rest/auth/login").excludePathPatterns("/rest/template/names").excludePathPatterns("/rest/template/templates");
        //manager authorization interceptor setup
        registry.addInterceptor(managerAuthInterceptor).addPathPatterns(ManagerAuthInterceptor.INTERCEPTED_URLS).excludePathPatterns(ManagerAuthInterceptor.EXCEPTED_URLS);
    }
}