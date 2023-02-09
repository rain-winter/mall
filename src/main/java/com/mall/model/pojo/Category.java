package com.mall.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
@Data
@TableName("mall_category") // 设置表明
public class Category {
    @TableId(type = IdType.AUTO) // 设置主键递增
    private Integer id;

    private String name;

    private Integer type;

    private Integer parentId;

    private String img;

    private Integer orderNum;

    private Date createTime;

    private Date updateTime;
}