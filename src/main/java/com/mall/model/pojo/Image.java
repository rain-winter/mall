package com.mall.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("mall_image") // 设置表明
public class Image {
    @TableId(type = IdType.AUTO) // 设置主键递增
    private Integer id;

    private String img_url;
}
