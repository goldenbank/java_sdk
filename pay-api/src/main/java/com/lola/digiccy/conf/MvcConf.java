package com.lola.digiccy.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MECHREVO
 */
@Configuration
public class MvcConf implements WebMvcConfigurer {
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludes =new ArrayList<>();
        excludes.add("/index");
        excludes.add("/api/getToken");
        excludes.add("/api/back");
        registry.addInterceptor(new Handle(contextPath)).addPathPatterns("/**").excludePathPatterns(excludes);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
