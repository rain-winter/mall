package com.mall.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mall.model.pojo.Order;
import com.mall.model.request.CreateOrderReq;
import com.mall.model.request.CreateOrderReqFormReq;
import com.mall.model.vo.OrderVO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 描述: 订单 Service
 */
public interface OrderService extends IService<Order> {


    String createOrderFromReq(CreateOrderReqFormReq req);

    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);

    //----------------------------------------------------------------------------------------------------
    PageInfo listForCustomer(Integer pageNum, Integer pageSize);

    // ----------------------------------------------------------------------------------------
    void cancel(String orderNo);

    String qrcode(String orderNo);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    void pay(String orderNo);

    void deliver(String orderNo);

    void finish(String orderNo);

    // ----------------------------------------------
}
