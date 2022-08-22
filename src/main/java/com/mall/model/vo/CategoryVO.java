package com.mall.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
public class CategoryVO {
    private Integer id;

    private String name;

    private Integer type;

    private Integer parentId;

    private Integer orderNum;

    private Date createTime;

    private Date updateTime;

    private List<CategoryVO> childCategory = new ArrayList();


}