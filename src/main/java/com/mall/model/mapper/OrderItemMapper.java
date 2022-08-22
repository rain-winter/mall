package com.mall.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.model.pojo.OrderItem;

import java.util.List;

public interface OrderItemMapper extends BaseMapper<OrderItem> {
    List<OrderItem> selectByOrderNo(String orderNo);
}
