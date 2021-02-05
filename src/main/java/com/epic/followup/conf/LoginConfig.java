package com.epic.followup.conf;

import com.epic.followup.interceptor.AdminLoginInterceptor;
import com.epic.followup.interceptor.SchoolInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {

    /**
     * 不需要登录拦截的url
     */
    private static final String[] notLoginInterceptPaths = { "/aidoctor/admin/index/login.html", "/aidoctor/static/**",
            "/aidoctor/admin/index/login.html", "/aidoctor/doctorManager/index.html", "/web/login" };

    @Bean
    public AdminLoginInterceptor getAdminLoginInterceptor() {
        return new AdminLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 0 超级管理员
        InterceptorRegistration addInterceptor = registry.addInterceptor(getAdminLoginInterceptor());
        addInterceptor.excludePathPatterns(notLoginInterceptPaths);

        // 动态路径拦截
        addInterceptor.addPathPatterns("/web/admin/**");

        // 静态资源拦截
        addInterceptor.addPathPatterns("/aidoctor/admin/**");

        // 校方管理系统的登录拦截
        InterceptorRegistration loginInterceptor = registry.addInterceptor(new SchoolInterceptor());
        loginInterceptor.excludePathPatterns("/school/user/login/loginByUserName");
        loginInterceptor.addPathPatterns("/school/**");
    }

}
