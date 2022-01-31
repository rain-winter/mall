package com.imooc.mall.config;

import com.imooc.mall.filter.AdminFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：  Admin过滤器的配置
 */
@Configuration
public class AdminFilterConfig {
    @Bean
    public AdminFilter adminFilter() {
        return new AdminFilter();
    }

    @Bean(name = "adminFilterConf")
    public FilterRegistrationBean adminFilterConfig() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(adminFilter());
        // 设置拦截的 URL。admin/分类接口 商品接口 订单接口都要拦截
        filterRegistrationBean.addUrlPatterns("/admin/category/*");
        filterRegistrationBean.addUrlPatterns("/admin/product/*");
        filterRegistrationBean.addUrlPatterns("/admin/order/*");
        // 给当前的Bean设置名字
        filterRegistrationBean.setName("adminFilterConfig");
        // 返回
        return filterRegistrationBean;

    }
}
