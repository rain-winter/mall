package com.mall.controller;

import com.mall.common.ApiRestRes;
import com.mall.model.pojo.User;
import com.mall.model.request.LoginReq;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ApiRestRes login(@RequestBody @Valid LoginReq loginReq){
//        User user = userService.login(loginReq.getUserName(), loginReq.getPassword());
//        user.setPassword(null); // 保存用户信息时不保存密码
//        System.out.println(name);
        return ApiRestRes.success();
    }
}
