package com.mall.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.ApiRestRes;
import com.mall.exception.MallException;
import com.mall.exception.MallExceptionEnum;
import com.mall.model.mapper.UserMapper;
import com.mall.model.pojo.User;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User login(String userName, String password) {
        System.out.println(userName);
        User user = userMapper.selectLogin(userName, password);
        if (user == null) {
            throw new MallException(MallExceptionEnum.WRONG_PASSWORD);
        }
        StpUtil.login(user.getId());
//        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
//        System.out.println(tokenInfo);
        return user;
    }
}
