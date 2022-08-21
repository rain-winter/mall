package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.ApiRestRes;
import com.mall.model.pojo.User;

public interface UserService extends IService<User> {
    User login(String userName, String password) ;
}
