package com.mall.model.query;

import lombok.Data;

import java.util.List;

/**
 * 查询商品列表的query
 */
@Data
public class ProductListQuery {

    private String keyword;

    private List<Integer> categoryIds;


}
