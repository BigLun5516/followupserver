package com.epic.followup.conf;


import com.epic.followup.interceptor.AdminLoginInterceptor;
import com.epic.followup.interceptor.Followup2Interceptor;
import com.epic.followup.service.followup2.BaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Followup2LoginConfig implements WebMvcConfigurer {

    /**
     * 不需要登录拦截的url
     */
    private static final String[] notLoginInterceptPaths = {"/static/**",
            "/followup2/stlogin", "/followup2/stRegist",
            "/followup2/getCode", // 获取短信验证码
            "/followup2/resetPassword", // 重置密码
            "/followup2/wechat/login"}; // 微信登录
    private BaseUserService baseUserService;

    @Autowired
    public Followup2LoginConfig(BaseUserService baseUserService){
        this.baseUserService = baseUserService;
    }

    @Bean
    public Followup2Interceptor getFollowup2Interceptor(){
        return new Followup2Interceptor(this.baseUserService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        InterceptorRegistration addInterceptor = registry.addInterceptor(getFollowup2Interceptor());
        addInterceptor.excludePathPatterns(notLoginInterceptPaths);
        // 动态路径拦截
        addInterceptor.addPathPatterns("/followup2/**");
        // 静态资源拦截 暂无
        addInterceptor.addPathPatterns("/followup2/**");

    }

}
