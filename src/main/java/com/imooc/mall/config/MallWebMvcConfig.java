package com.imooc.mall.config;

import com.imooc.mall.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MallWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //  以admin/**底下的文件定向到 /static/admin/
        // 这里配置的是后台管理系统
        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/static/admin/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + Constant.FILE_UPLOAD_DIR);
    }

    /**
     *
     *  跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  //设置允许跨域访问的路径
                .allowedOriginPatterns("*")  //设置允许跨域访问的源
                .allowedMethods("*")  //允许跨域请求的方法
                .maxAge(168000)  //预检间隔时间
                .allowedHeaders("*")  //允许头部设置
                .allowCredentials(true);  //是否发送 cookie
    }

}
