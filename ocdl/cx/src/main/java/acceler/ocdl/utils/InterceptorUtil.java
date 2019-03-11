//package acceler.ocdl.utils;
//
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//import javax.annotation.Resource;
//
//public class InterceptorUtil extends WebMvcConfigurerAdapter {
//
//    @Resource
//    private AuthInterceptor authInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor).addPathPatterns("test/api/**").excludePathPatterns("/api/auth/login");
//    }
//}
