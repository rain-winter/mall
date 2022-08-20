package com.mall.fetcher;

import com.mall.common.ApiRestRes;
import com.mall.exception.MallExceptionEnum;
import com.mall.model.pojo.User;
import com.mall.service.UserService;
import com.mall.type.LoginInput;
import com.mysql.cj.util.StringUtils;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

@DgsComponent
public class UserFetcher {

    @Autowired
    UserService userService;

    @DgsQuery
    public String hello(@InputArgument String name) {
        return "你好"+"name";
    }

//    @DgsMutation
//    public ApiRestRes login(@InputArgument LoginInput loginInput){
//        if (StringUtils.isNullOrEmpty(loginInput.getUserName())) {
//            return ApiRestRes.error(MallExceptionEnum.NEED_USER_NAME);
//        }
//        if (StringUtils.isNullOrEmpty(loginInput.getPassword())) {
//            return ApiRestRes.error(MallExceptionEnum.NEED_PASSWORD);
//        }
//        User user = userService.login(loginInput.getUserName(), loginInput.getPassword());
//        return ApiRestRes.success(user);
//    }

    @DgsMutation
    public User login(@InputArgument LoginInput loginInput){
//        if (StringUtils.isNullOrEmpty(loginInput.getUserName())) {
//            return ApiRestRes.error(MallExceptionEnum.NEED_USER_NAME);
//        }
//        if (StringUtils.isNullOrEmpty(loginInput.getPassword())) {
//            return ApiRestRes.error(MallExceptionEnum.NEED_PASSWORD);
//        }
        User user = userService.login(loginInput.getUserName(), loginInput.getPassword());
        return user;
    }


//    public ApiRestRes login(@RequestParam("userName") String userName, @RequestParam("password") String password){
//        if (StringUtils.isNullOrEmpty(userName)) {
//            return ApiRestRes.error(MallExceptionEnum.NEED_USER_NAME);
//        }
//        if (StringUtils.isNullOrEmpty(password)) {
//            return ApiRestRes.error(MallExceptionEnum.NEED_PASSWORD);
//        }
//        User user = userService.login(userName, password);
//        user.setPassword(null); // 保存用户信息时不保存密码
//
//        return null;
//    }

}
