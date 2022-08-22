package com.mall.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateProductReq {

    @NotNull
    private Integer id;

    private String name;

    private String image;

    private String detail;

    private Integer categoryId;

    private Integer price;

    private Integer stock;

    private Integer status;

}