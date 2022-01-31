package com.imooc.mall.service.impl;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.UserMapper;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;

import com.imooc.mall.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * 描述： UserService 实现类
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User getUser() {
        return userMapper.selectByPrimaryKey(1);
    }

    @Override
    public void register(String userName, String password)
            throws ImoocMallException {
        // 查询用户名是否存在，不允许重名
        User result = userMapper.selectByName(userName);
        if (result != null) {
            // 用户已存在，抛出异常 。在GlobalExceptionHandler里处理
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }
        // 写到数据库
        User user = new User();
        user.setUsername(userName);
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        int count = userMapper.insertSelective(user); // 方法返回的 行数，为1插入成功
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public User login(String userName, String password) throws ImoocMallException {
        String md5Password = null;
        try {
            md5Password = MD5Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        User user = userMapper.selectLogin(userName, md5Password);
        if (user == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }

    @Override
    public void updateInformation(User user) {
        // 更新个人签名
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 1) {
            // ===1 true   >1 false
            try {
                throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
            } catch (ImoocMallException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean checkAdminRole(User user) {
        // 1 普通用户  2 管理员
        return user.getRole().equals(2);
    }

}
