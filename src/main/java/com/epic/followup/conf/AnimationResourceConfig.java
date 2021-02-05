package com.epic.followup.conf;

/**
 * @author : zx
 * @version V1.0
 */

import com.epic.followup.interceptor.ResourceInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *  配置静态资源拦截器
 */

@Configuration
public class AnimationResourceConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ResourceInterceptor()).excludePathPatterns("/animations/**");
        registry.addInterceptor(new ResourceInterceptor()).excludePathPatterns("/aidoctor/**");
        registry.addInterceptor(new ResourceInterceptor()).excludePathPatterns("/h5/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 使用文件路径访问动画资源
        registry.addResourceHandler("/animations/**").addResourceLocations("file:./animations/");
        registry.addResourceHandler("/h5/**").addResourceLocations("file:./h5/");
        // 网页资源
        registry.addResourceHandler("/aidoctor/**").addResourceLocations("file:./static/");
        registry.addResourceHandler("/static/**").addResourceLocations("file:./static/");
    }

    /*
     * 设置允许跨域
     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/h5/**")
//                .allowedOrigins("*")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
//                .maxAge(3600 * 24);
//    }

}
