package com.imooc.mall.service;

import com.imooc.mall.vo.CartVO;

import java.util.List;

/**
 * 描述: 购物车 Service
 */
public interface CartService {
    List<CartVO> update(Integer userId, Integer productId, Integer count);

    List<CartVO> add(Integer userId, Integer productId, Integer count);

    List<CartVO> delete(Integer userId, Integer productId);

    List<CartVO> list(Integer userId);
}
