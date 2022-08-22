package com.mall.model.vo;

import lombok.Data;

/**
 * 描述: 给前端展示用
 * 拥有cartPojo的成员和价格、总价格、商品名子、商品总价
 */
@Data
public class CartVO {
    private Integer id;

    private Integer productId;

    private Integer userId;

    private Integer quantity;

    private Integer selected;

    private Integer price;

    private Integer totalPrice;

    private String productName;

    private String productImage;


}
