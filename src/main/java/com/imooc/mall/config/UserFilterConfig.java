package com.imooc.mall.config;

import com.imooc.mall.filter.UesrFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * 描述：  Admin过滤器的配置
 */
@Configuration
public class UserFilterConfig {
    @Bean
    public UesrFilter uesrFilter() {
        return new UesrFilter();
    }

    @Bean(name = "userFilterConf")
    public FilterRegistrationBean adminFilterConfig() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(uesrFilter());
        // 设置拦截的 URL。只需拦截订单模块，购物车模块
        filterRegistrationBean.addUrlPatterns("/cart/*");
        filterRegistrationBean.addUrlPatterns("/order/*");
        // 给当前的Bean设置名字
        filterRegistrationBean.setName("userFilterConfig");
        // 返回
        return filterRegistrationBean;
    }
}
