package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.exception.MallException;
import com.mall.model.pojo.User;

public interface UserService extends IService<User> {
    User login(String userName, String password) ;

    void register(String userName, String password)
            throws MallException;

    void updateInformation(User user);

    boolean checkAdminRole(User user);
}
