package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor()).
                addPathPatterns("/**").//默认先拦截所有
                excludePathPatterns(//需要放行的接口
                        "/admin/login",
                        "/admin/register",
                        "/swagger-ui/**",
                        "/v3/api-docx/**",
                        "/**.html",
                        "/js/**",
                        "/css/**",
                        "/img/**");
    }
}
