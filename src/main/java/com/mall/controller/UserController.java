package com.mall.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.mall.common.ApiRestRes;
import com.mall.exception.MallExceptionEnum;
import com.mall.model.pojo.User;
import com.mall.service.UserService;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/isLogin")
    public String addUser(){
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    @PostMapping("/login")
    public ApiRestRes login(@RequestParam("userName") String userName, @RequestParam("password") String password){
        if (StringUtils.isNullOrEmpty(userName)) {
            return ApiRestRes.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return ApiRestRes.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        user.setPassword(null); // 保存用户信息时不保存密码

        return null;
    }

}
