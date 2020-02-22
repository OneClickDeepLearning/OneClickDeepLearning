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
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/rest/**")
                .excludePathPatterns("/rest/auth/signup")
                .excludePathPatterns("/rest/auth/login")
                .excludePathPatterns("/rest/auth/key")
                .excludePathPatterns("/rest/auth");
                //.excludePathPatterns("/rest/template/**")
                //.excludePathPatterns("/rest/project/config")
                //.excludePathPatterns("/rest/persistence/*")
                //.excludePathPatterns("/rest/persistence");

        //manager authorization interceptor setup
        registry.addInterceptor(managerAuthInterceptor)
                .addPathPatterns(ManagerAuthInterceptor.INTERCEPTED_URLS)
                .excludePathPatterns(ManagerAuthInterceptor.EXCEPTED_URLS);
    }
}