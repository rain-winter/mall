package com.mall.controller;

import com.github.pagehelper.PageInfo;
import com.mall.common.ApiRestRes;
import com.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 订单后台管理
 */
@RestController
@Validated // 必须加上这个注解才可以验证
public class OrderAdminController {
    @Autowired
    OrderService orderService;

    @GetMapping("admin/order/list")
    public ApiRestRes listForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);
        return ApiRestRes.success(pageInfo);
    }

    /**
     * 发货。 订单状态流程：0用户取消， 10未付款， 20已付款， 30已发货， 40交易完成
     * @param orderNo 订单号
     */
    @PostMapping("admin/order/delivered")
    public ApiRestRes delivered(@RequestParam @NotBlank(message = "订单号不能为空") String orderNo){
        orderService.deliver(orderNo);
        return ApiRestRes.success();
    }

    /**
     * 完结订单 管理员和用户都可以调用
     * @param orderNo 订单号
     */
    @PostMapping("order/finish")
    public ApiRestRes finish(@RequestParam @NotBlank(message = "订单号不能为空") String orderNo){
        orderService.finish(orderNo);
        return ApiRestRes.success();
    }


}
