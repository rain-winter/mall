package com.mall.model.request;

import lombok.Data;

@Data
public class ProductListReq {

    private String keyword;

    private Integer categoryId;

    private String orderBy;

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}