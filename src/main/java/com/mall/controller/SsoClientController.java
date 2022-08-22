package com.mall.controller;

import cn.dev33.satoken.sso.SaSsoManager;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SsoClientController {

    // SSO-Client端：首页
    @RequestMapping("/")
    public String index() {
        String authUrl = SaSsoManager.getConfig().getAuthUrl();
        String solUrl = SaSsoManager.getConfig().getSloUrl();
        String str = "<h2>Sa-Token SSO-Client 应用端</h2>" +
                "<p>当前会话是否登录：" + StpUtil.isLogin() + "</p>" +
                "<p><a href=\"javascript:location.href='" + authUrl + "?mode=simple&redirect=' + encodeURIComponent(location.href);\">登录</a> " +
                "<a href=\"javascript:location.href='" + solUrl + "?back=' + encodeURIComponent(location.href);\">注销</a> </p>";
        return str;
    }
}
