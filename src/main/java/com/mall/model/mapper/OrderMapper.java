package com.mall.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.model.pojo.Order;

import java.util.List;

public interface OrderMapper  extends BaseMapper<Order> {
    // 根据
    Order selectByOrderNo(String orderNo);

    List<Order> selectForCustomer(Integer userId);
    List<Order> selectAllForAdmin();
}
