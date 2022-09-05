package com.mall.filter;
/**
 * 描述： 用户过滤器
 */

import cn.dev33.satoken.stp.StpUtil;
import com.mall.model.mapper.UserMapper;
import com.mall.model.pojo.User;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class UserFilter implements Filter {

    // 获取当前user，并存档
    public static User currentUser;

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();

        if (StpUtil.isLogin()) {
            currentUser = userMapper.selectById(StpUtil.getLoginIdAsInt());
        }
        if (currentUser == null) {
            // 这个方法规定是 void ，所以不可以返回数据
            //return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
            // 判断如果当前没有用户登录，
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n"
                    + "    \"status\": 10007,\n"
                    + "    \"msg\": \"NEED_LOGIN\",\n"
                    + "    \"data\": null\n"
                    + "}");
            out.flush();
            out.close();
            return; // 方法到这就结束了。
        }
        //  调用这个方法
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
