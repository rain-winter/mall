package com.mall.controller;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.mall.common.ApiRestRes;
import com.mall.common.Constant;
import com.mall.exception.MallException;
import com.mall.exception.MallExceptionEnum;
import com.mall.model.mapper.UserMapper;
import com.mall.model.pojo.User;
import com.mall.model.request.LoginReq;
import com.mall.service.UserService;
import com.mysql.cj.util.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@Validated // 必须加上这个注解才可以验证
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @PostMapping("/login")
    public ApiRestRes login(@RequestBody @Valid LoginReq loginReq, HttpSession session) {
        User user = userService.login(loginReq.getUserName(), loginReq.getPassword());
        user.setPassword(null); // 保存用户信息时不保存密码
        // 第3步，返回给前端
        StpUtil.login(user.getId()); // 登录
        session.setAttribute(Constant.IMOOC_MALL_USER, user);
        return ApiRestRes.success(user);
    }


    @PostMapping("/register")
    @ResponseBody
    public ApiRestRes register(@RequestParam("userName") @NotBlank String userName, @RequestParam("password") @NotBlank String password) throws MallException {

//        System.out.println(password);
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
        StpUtil.logout();
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
//        System.out.println("session--------------------" + session);
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
            // TODO session貌似不能生效
//            session.setAttribute(Constant.IMOOC_MALL_USER, user);
            // TODO 设置token
            StpUtil.login(user.getId());
        } else {
            return ApiRestRes.error(MallExceptionEnum.NEED_ADMIN);
        }
        return ApiRestRes.success(user);
    }

    @GetMapping("/isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    @GetMapping("/admin/userlist")
    public ApiRestRes userList() {
        List userList = userService.list();
        return ApiRestRes.success(userList);
    }

    @PostMapping("/user/wxlogin")
    public ApiRestRes wxLogin(@RequestBody String openid) {
        User user = userMapper.selectByOpenId(openid);

        if (user==null) {
            User currentUser = new User();
            currentUser.setPassword(SaSecureUtil.md5BySalt("123456", Constant.SALT));
            currentUser.setOpenid(openid);
            int id = userMapper.insert(currentUser);
            StpUtil.login(id);
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return ApiRestRes.success(SaResult.data(tokenInfo));
        }else {
            StpUtil.login(user.getId());
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return ApiRestRes.success(SaResult.data(tokenInfo));
        }
    }




}
