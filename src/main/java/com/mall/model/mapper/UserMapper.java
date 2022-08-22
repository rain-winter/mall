package com.mall.model.mapper;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.model.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {
    User selectLogin(@Param("userName") String userName, @Param("password") String password);

    User selectByName(@Param("userName") String userName);
}


