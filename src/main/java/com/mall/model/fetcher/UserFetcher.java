package com.mall.model.fetcher;

import com.mall.model.pojo.User;
import com.mall.service.UserService;
import com.mall.type.LoginInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * TODO: 尝试用Graphql实现api，遇到统一异常放弃
 */
@DgsComponent
public class UserFetcher {

    @Autowired
    UserService userService;

    @DgsQuery
    public String hello(@NotNull @InputArgument("name") String name) {
        return "你好" + name;
    }

    @DgsMutation
    public User login(@InputArgument @Valid LoginInput loginInput) {
        User user;
        user = userService.login(loginInput.getUserName(), loginInput.getPassword());
        return user;
    }


}
