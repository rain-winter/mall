package com.mall.controller;

import com.github.pagehelper.PageInfo;
import com.mall.common.ApiRestRes;
import com.mall.model.request.CreateOrderReq;
import com.mall.service.OrderService;
import com.mall.model.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：  订单
 */
@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    /**
     * 创建订单
     *
     * @param createOrderReq
     * @return
     */
    @PostMapping("order/create")
    public ApiRestRes create(@RequestBody CreateOrderReq createOrderReq) {
        String orderNo = orderService.create(createOrderReq);
        return ApiRestRes.success(orderNo);
    }

    // 查看订单详情
    @GetMapping("order/detail")
    public ApiRestRes detail(@RequestParam String orderNo) {
        OrderVO orderVO = orderService.detail(orderNo);
        return ApiRestRes.success(orderVO);
    }

    @GetMapping("order/list")
    public ApiRestRes list(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageInfo = orderService.listForCustomer(pageNum, pageSize);
        return ApiRestRes.success(pageInfo);
    }

    @PostMapping("order/cancel")
    public ApiRestRes cancel(@RequestParam String orderNo) {
        orderService.cancel(orderNo);
        return ApiRestRes.success();
    }

    @PostMapping("order/qrcode")
    public ApiRestRes qrcode(@RequestParam String orderNo){
      String pngAddress =  orderService.qrcode(orderNo);
        return ApiRestRes.success(pngAddress);
    }

    @GetMapping("order/pay")
    public ApiRestRes pay(@RequestParam String orderNo){
        orderService.pay(orderNo);
        return ApiRestRes.success();
    }

}
