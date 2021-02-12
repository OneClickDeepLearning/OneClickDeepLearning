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
    private ProjectAuthInterceptor projectAuthInterceptor;

    @Resource
    private ManagerAuthInterceptor managerAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/rest/**")
                .excludePathPatterns("/rest/auth/signup")
                .excludePathPatterns("/rest/auth/login")
                .excludePathPatterns("/rest/auth/key")
                .excludePathPatterns("/rest/auth")
                // for vul app upload tagged data
                .excludePathPatterns("/rest/project/projectdata/recycle");

        //manager authorization interceptor setup
        registry.addInterceptor(projectAuthInterceptor)
                .addPathPatterns(ProjectAuthInterceptor.INTERCEPTED_URLS)
                .excludePathPatterns(ProjectAuthInterceptor.EXCEPTED_URLS);


        //manager authorization interceptor setup
        registry.addInterceptor(managerAuthInterceptor)
                .addPathPatterns(ManagerAuthInterceptor.INTERCEPTED_URLS)
                .excludePathPatterns(ManagerAuthInterceptor.EXCEPTED_URLS);
    }
}