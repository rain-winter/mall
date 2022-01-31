package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.User;

/**
 * 描述：  UserService
 */

public interface UserService {
    User getUser();
    // 注册
    void register(String userName,String password) throws ImoocMallException;
    // 登录
    User login(String userName, String password) throws ImoocMallException;

    void updateInformation(User user);

    boolean checkAdminRole(User user);
}
