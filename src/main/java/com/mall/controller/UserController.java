package com.mall.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.mall.common.ApiRestRes;
import com.mall.exception.MallException;
import com.mall.exception.MallExceptionEnum;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/islogin")
    public String isLogin(){
        return StpUtil.getLoginId("没有登录");
    }

    @GetMapping("/login")
    public Object login(){
        // 第1步，先登录上
        StpUtil.login(10001);
        // 第2步，获取 Token  相关参数
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 第3步，返回给前端
        return SaResult.data(tokenInfo);
    }

    @PostMapping("/login")
    public ApiRestRes login(@RequestBody @Valid LoginReq loginReq){
        Map res = new HashMap();
        User user = userService.login(loginReq.getUserName(), loginReq.getPassword());
        user.setPassword(null); // 保存用户信息时不保存密码
        ;
        res.put("user",user);
        res.put("token",SaResult.data(StpUtil.getTokenValue()));
        // 第3步，返回给前端
        return ApiRestRes.success(res);
    }

    @PostMapping("/register")
    @ResponseBody
    public ApiRestRes register(@RequestParam("userName") @NotBlank String userName, @RequestParam("password") @NotBlank String password) throws MallException {

        System.out.println(password);
//        // 密码不能小于八位。使用注解验证了，这里就不需要了
//        if (password.length() < 8) {
//            return ApiRestRes.error(MallExceptionEnum.PASSWORD_TO_SHORT);
//        }
        userService.register(userName, password);
        return ApiRestRes.success();
    }
}
