package com.mall.controller;

import com.mall.common.ApiRestRes;
import com.mall.common.Constant;
import com.mall.exception.MallException;
import com.mall.exception.MallExceptionEnum;
import com.mall.model.pojo.User;
import com.mall.model.request.LoginReq;
import com.mall.service.UserService;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ApiRestRes login(@RequestBody @Valid LoginReq loginReq, HttpSession session) {
        User user = userService.login(loginReq.getUserName(), loginReq.getPassword());
        user.setPassword(null); // 保存用户信息时不保存密码
        // 第3步，返回给前端
        session.setAttribute(Constant.IMOOC_MALL_USER, user);
        return ApiRestRes.success(user);
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

    /**
     * 更新个性签名
     *
     * @param session
     * @param signature
     * @return
     */
    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestRes updateUserInfo(HttpSession session, @RequestParam("signature") String signature) {
        User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        if (currentUser == null) {
            return ApiRestRes.error(MallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestRes.success();
    }

    /**
     * 退出
     *
     * @param session
     * @return
     */
    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestRes logout(HttpSession session) {
        // 在 session中删除
        session.removeAttribute(Constant.IMOOC_MALL_USER);
        return ApiRestRes.success();
    }

    /**
     * 管理员登录
     *
     * @param userName
     * @param password
     * @param session
     * @return
     * @throws MallException
     */
    @PostMapping("/adminlogin")
    @ResponseBody
    public ApiRestRes adminLogin(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session) throws MallException {
        if (StringUtils.isNullOrEmpty(userName)) {
            return ApiRestRes.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return ApiRestRes.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password); // 调用service层的登录方法

        if (userService.checkAdminRole(user)) {
            // 是管理员 登录
            user.setPassword(null);
            session.setAttribute(Constant.IMOOC_MALL_USER, user);
        } else {
            return ApiRestRes.error(MallExceptionEnum.NEED_ADMIN);
        }
        return ApiRestRes.success(user);
    }
}
