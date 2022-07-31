package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.service.CartService;
import com.imooc.mall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：  购物车
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/list")
    public ApiRestResponse list() {
        // 内部获取user id，防止横向越权
        List<CartVO> cartList = cartService.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(cartList);
    }

    @PostMapping("/add")
    public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count) {
        // 从User过滤器中获取当前用户
        List<CartVO> cartVOList = cartService.add(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 更新购物车
     * @param productId 商品id
     * @param count 上坪数量
     * @return
     */
    @PostMapping("/update")
    public ApiRestResponse update(@RequestParam Integer productId, @RequestParam Integer count) {
        // 从User过滤器中获取当前用户
        List<CartVO> cartVOList = cartService.update(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/delete")
    public ApiRestResponse delete(@RequestParam() Integer productId){
 // 不能传入userID cartID。否则可以删除别人的购物车
        List<CartVO> cartVOList =cartService.delete(UserFilter.currentUser.getId(),productId);
        return ApiRestResponse.success(cartVOList);
    }
}
