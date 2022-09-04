package com.mall.filter;

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

public class AdminFilter implements Filter {

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

    /**
     * 这个方法没进入控制器之前执行
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        // TODO 不能从session获取到 user
//        User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        // 获取用户id
        Integer id = StpUtil.getLoginIdAsInt();
        System.out.println("id-----------------"+id);

        if (id == null) {
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
        User currentUser = userMapper.selectById(id);
        // 当前用户用户是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (adminRole) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n"
                    + "    \"status\": 10009,\n"
                    + "    \"msg\": \"无管理员权限\",\n"
                    + "    \"data\": null\n"
                    + "}");
            out.flush();
            out.close();
        }

    }
}
