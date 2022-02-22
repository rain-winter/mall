package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：  购物车
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @PostMapping("/add")
    public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count) {
        return null;
    }
}
