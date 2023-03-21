package com.mall.controller;

import com.github.pagehelper.PageInfo;
import com.mall.common.ApiRestRes;
import com.mall.model.request.CreateOrderReq;
import com.mall.model.request.CreateOrderReqFormReq;
import com.mall.service.OrderService;
import com.mall.model.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * 描述：  订单
 */
@RestController
@Validated // 必须加上这个注解才可以验证
public class OrderController {
    @Autowired
    OrderService orderService;

    /**
     * 不加入购物车，直接创建订单
     * @param createOrderReqFormReq
     * @return
     */
    @PostMapping("order/createFromReq")
    public ApiRestRes createOrderFromReq(@RequestBody @Valid CreateOrderReqFormReq createOrderReqFormReq){
        String orderNo = orderService.createOrderFromReq(createOrderReqFormReq);
        return ApiRestRes.success(orderNo);
    }

    /**
     * 创建订单
     *
     * @param createOrderReq
     * @return
     */
    @PostMapping("order/create")
    public ApiRestRes create(@RequestBody @Valid CreateOrderReq createOrderReq) {
        String orderNo = orderService.create(createOrderReq);
        return ApiRestRes.success(orderNo);
    }

    @PostMapping("order/createDirectly")
    public  ApiRestRes CreateOrderDirectly(){
        return null;
    }

    // 查看订单详情
    @GetMapping("order/detail")
    public ApiRestRes detail(@RequestParam @NotEmpty(message = "订单号不能为空") String orderNo) {
        OrderVO orderVO = orderService.detail(orderNo);
        return ApiRestRes.success(orderVO);
    }

    @GetMapping("order/list")
    public ApiRestRes list(@RequestParam Integer pageNum, @RequestParam  Integer pageSize) {
        PageInfo pageInfo = orderService.listForCustomer(pageNum, pageSize);
        return ApiRestRes.success(pageInfo);
    }

    @PostMapping("order/cancel")
    public ApiRestRes cancel(@RequestParam @NotBlank(message = "订单号不能为空") String orderNo) {
        orderService.cancel(orderNo);
        return ApiRestRes.success();
    }

    @PostMapping("order/qrcode")
    public ApiRestRes qrcode(@RequestParam @NotBlank(message = "订单号不能为空") String orderNo) {
        String pngAddress = orderService.qrcode(orderNo);
        return ApiRestRes.success(pngAddress);
    }

    @GetMapping("order/pay")
    public ApiRestRes pay(@RequestParam @NotBlank(message = "订单号不能为空") String orderNo) {
        orderService.pay(orderNo);
        return ApiRestRes.success();
    }

}
