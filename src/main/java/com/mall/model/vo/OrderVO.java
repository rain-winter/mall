package com.mall.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class OrderVO {

    private String orderNo;

    private String orderStatusName; // 新加的

    private List<OrderItemVO> orderItemVOList; // 新加的

    private Integer userId;

    private Integer totalPrice;

    private String receiverName;

    private String receiverMobile;

    private String receiverAddress;

    private Integer orderStatus;

    private Integer postage;

    private Integer paymentType;

    private Date deliveryTime;

    private Date payTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

}