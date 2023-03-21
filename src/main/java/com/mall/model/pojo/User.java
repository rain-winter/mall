package com.mall.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import java.util.Date;

// lombok 插件。使用该注解便可以实现setter/getter/
@Data
@ToString
@TableName("mall_user") // 设置表明
public class User {
    @TableId(type = IdType.AUTO) // 设置主键递增
    private Integer id;

    private String username;

    private String password;

    private String openid;

    private String personalizedSignature;

    private Integer role;

    private Date createTime;

    private Date updateTime;
}
