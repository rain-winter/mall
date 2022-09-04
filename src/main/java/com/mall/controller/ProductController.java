package com.mall.controller;

import com.github.pagehelper.PageInfo;
import com.mall.common.ApiRestRes;
import com.mall.model.pojo.Product;
import com.mall.model.request.ProductListReq;
import com.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 给用户看的列表
 */
@RestController
@Validated
public class ProductController {

    @Autowired
    ProductService productService;

    /**
     * 商品详情
     *
     * @param id 商品id
     * @return 一个商品
     */
    @GetMapping("product/detail")
    public ApiRestRes detail(@RequestParam Integer id) {
        Product product = productService.detail(id);
        return ApiRestRes.success(product);
    }

    /**
     * @param productListReq
     * @return
     */
    @GetMapping("product/list")
    public ApiRestRes list(ProductListReq productListReq) {
        System.out.println(productListReq);
        PageInfo list = productService.list(productListReq);
        return ApiRestRes.success(list);
    }
}
