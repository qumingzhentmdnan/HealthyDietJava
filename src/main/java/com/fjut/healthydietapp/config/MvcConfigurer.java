package com.fjut.healthydietapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer {
    @Autowired
    private Interceptor interceptor;
    //除了登录注册和获取token接口，其他接口都需要登录
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).
                addPathPatterns("/**").
                excludePathPatterns("/user/login").
                excludePathPatterns("/user/register").
                excludePathPatterns("/user/getCode").
                excludePathPatterns("/user/updatePasswordByCode").
                excludePathPatterns("/user/token").
                excludePathPatterns("/chat/download");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}