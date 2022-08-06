package com.imooc.mall.service;


import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.vo.OrderVO;

/**
 * 描述: 订单 Service
 */
public interface OrderService {

    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);
}
