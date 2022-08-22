package com.mall.config;

import com.mall.filter.UserFilter;
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
    public UserFilter uesrFilter() {
        return new UserFilter();
    }

    @Bean(name = "userFilterConf")
    public FilterRegistrationBean userFilterConfig() {
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
