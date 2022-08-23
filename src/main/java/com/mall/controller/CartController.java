package com.mall.controller;

import com.mall.common.ApiRestRes;
import com.mall.exception.MallExceptionEnum;
import com.mall.filter.UserFilter;
import com.mall.service.CartService;
import com.mall.model.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：  购物车
 */
@RestController
@RequestMapping("/cart")
@Validated // 必须加上这个注解才可以验证
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/list")
    public ApiRestRes list() {
        // 内部获取user id，防止横向越权
        List<CartVO> cartList = cartService.list(UserFilter.currentUser.getId());
        return ApiRestRes.success(cartList);
    }

    @PostMapping("/add")
    public ApiRestRes add(@RequestParam Integer productId, @RequestParam Integer count) {
        // 从User过滤器中获取当前用户
        List<CartVO> cartVOList = cartService.add(UserFilter.currentUser.getId(), productId, count);
        return ApiRestRes.success(cartVOList);
    }

    /**
     * 更新购物车
     *
     * @param productId 商品id
     * @param count     上坪数量
     * @return
     */
    @PatchMapping("/update")
    public ApiRestRes update(@RequestParam  Integer productId, @RequestParam Integer count) {
        // 从User过滤器中获取当前用户
        List<CartVO> cartVOList = cartService.update(UserFilter.currentUser.getId(), productId, count);
        return ApiRestRes.success(cartVOList);
    }

    /**
     * 删除购物车
     *
     * @param productId 商品id
     * @return
     */
    @PostMapping("/delete")
    public ApiRestRes delete(@RequestParam Integer productId) {
        // 不能传入userID cartID。否则可以删除别人的购物车
        List<CartVO> cartVOList = cartService.delete(UserFilter.currentUser.getId(), productId);
        return ApiRestRes.success(cartVOList);
    }

    /**
     * 选中/不选中商品
     * @param productId 商品id
     * @param selected 0 1
     * @return
     */
    @PostMapping("/select")
    public ApiRestRes select(@RequestParam() Integer productId, @RequestParam Integer selected) {
        // 不能传入userID cartID。否则可以删除别人的购物车
        // 1选中 0不选
        if (selected != 0 && selected != 1) {
            return ApiRestRes.error(MallExceptionEnum.UPDATE_FAILED);
        }
        List<CartVO> cartVOList = cartService.selectOrNot(UserFilter.currentUser.getId(), productId, selected);
        return ApiRestRes.success(cartVOList);
    }

    /**
     * 全选
     * @param selected
     * @return
     */
    @PostMapping("/selectAll")
    public ApiRestRes selectAll(@RequestParam() Integer selected) {
        if (selected != 0 && selected != 1) {
            return ApiRestRes.error(MallExceptionEnum.UPDATE_FAILED);
        }
        List<CartVO> cartVOList = cartService.selectAllOrNot(UserFilter.currentUser.getId(), selected);
        return ApiRestRes.success(cartVOList);
    }
}
