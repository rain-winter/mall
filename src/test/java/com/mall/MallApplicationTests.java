package com.mall;

import com.mall.model.mapper.UserMapper;
import com.mall.model.pojo.User;
import com.mall.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
    void testMapper() {
        User user = new User();
        user.setEmail("310040@qq.com");
        user.setPassword("1234566");
        user.setRole(1);
        user.setPersonalizedSignature("123456");
        userMapper.insert(user);
    }

    @Test
    void testIService() {
        System.out.println("总记录数："+userService.count());
    }

}
