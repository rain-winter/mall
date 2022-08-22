package com.mall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MallWebMvcConfig implements WebMvcConfigurer {

    /**
     * 跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")  //设置允许跨域访问的路径
                .allowedOriginPatterns("*")  //设置允许跨域访问的源
                .allowedMethods("*")  //允许跨域请求的方法
                .maxAge(168000)  //预检间隔时间
                .allowedHeaders("*")  //允许头部设置
                .allowCredentials(true);  //是否发送 cookie
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}
