package com.imooc.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

/**
 * 让boot知道mapper在哪
 */
@MapperScan(basePackages = "com.imooc.mall.model.dao")
public class ImoocMallApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImoocMallApplication.class, args);
    }
}
