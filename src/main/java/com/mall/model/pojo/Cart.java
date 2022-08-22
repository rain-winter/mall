package com.mall.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
@Data
@TableName("mall_cart") // 设置表明
public class Cart {
    @TableId(type = IdType.AUTO) // 设置主键递增
    private Integer id;

    private Integer productId;

    private Integer userId;

    private Integer quantity;

    private Integer selected;

    private Date createTime;

    private Date updateTime;

}