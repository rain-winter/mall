package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByName(String userName);

    /**
     * 查找密码的方法。有两个参数要加注解了
     * @param userName 用户名
     * @param password 密码
     * @return user
     */
    User selectLogin(@Param("userName") String userName, @Param("password")String password);
}