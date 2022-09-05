package com.mall.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import com.mall.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MallWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //  以admin/**底下的文件定向到 /static/admin/
        // 这里配置的是后台管理系统
        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/static/admin/");
        System.out.println(Constant.FILE_UPLOAD_DIR);
//
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + Constant.FILE_UPLOAD_DIR);

// 处理jquery
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//        super.addResourceHandlers(registry);
    }


    /**
     * 跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")  //设置允许跨域访问的路径
                .allowedHeaders("*")  //允许头部设置
                .allowedMethods("*")  //允许跨域请求的方法
                .allowCredentials(true)
                .maxAge(168000)  // 预检间隔时间
                .allowedOriginPatterns("*");

    }

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 的路由拦截器
        registry.addInterceptor(new SaRouteInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/adminlogin","/login");
    }
}
