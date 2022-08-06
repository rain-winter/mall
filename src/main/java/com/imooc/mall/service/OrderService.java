package com.imooc.mall.service;


import com.github.pagehelper.PageInfo;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.vo.OrderVO;

/**
 * 描述: 订单 Service
 */
public interface OrderService {

    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);

    //----------------------------------------------------------------------------------------------------
    PageInfo listForCustomer(Integer pageNum, Integer pageSize);

    // ----------------------------------------------------------------------------------------
    void cancel(String orderNo);
}
