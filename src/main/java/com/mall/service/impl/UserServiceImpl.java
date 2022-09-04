package com.mall.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.Constant;
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
        User user = userMapper.selectLogin(userName, SaSecureUtil.md5BySalt(password,Constant.SALT));
        if (user == null) {
            throw new MallException(MallExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }

    @Override
    public void register(String userName, String password)
            throws MallException {
        // 查询用户名是否存在，不允许重名
        User result = userMapper.selectByName(userName);
        if (result != null) {
            // 用户已存在，抛出异常 。在GlobalExceptionHandler里处理
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        // 写到数据库
        User user = new User();
        user.setUsername(userName);
        user.setPassword(SaSecureUtil.md5BySalt(password, Constant.SALT));

        int count = userMapper.insert(user); // 方法返回的 行数，为1插入成功
        if (count == 0) {
            throw new MallException(MallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public void updateInformation(User user) {
        // 更新个人签名
        int updateCount = userMapper.updateById(user);
//        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 1) {
            // ===1 true   >1 false
            try {
                throw new MallException(MallExceptionEnum.UPDATE_FAILED);
            } catch (MallException e) {
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
