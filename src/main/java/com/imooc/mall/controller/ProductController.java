package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.ProductListReq;
import com.imooc.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 给用户看的列表
 */
@RestController
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
    public ApiRestResponse detail(@RequestParam Integer id) {
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }

    /**
     * @param productListReq
     * @return
     */
    @GetMapping("product/list")
    public ApiRestResponse list(ProductListReq productListReq) {
        System.out.println(productListReq);
        PageInfo list = productService.list(productListReq);
        return ApiRestResponse.success(list);
    }
}
