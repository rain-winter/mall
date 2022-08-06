package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.service.CartService;
import com.imooc.mall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
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
     *
     * @param productId 商品id
     * @param count     上坪数量
     * @return
     */
    @PatchMapping("/update")
    public ApiRestResponse update(@RequestParam Integer productId, @RequestParam Integer count) {
        // 从User过滤器中获取当前用户
        List<CartVO> cartVOList = cartService.update(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 删除购物车
     *
     * @param productId 商品id
     * @return
     */
    @PostMapping("/delete")
    public ApiRestResponse delete(@RequestParam() Integer productId) {
        // 不能传入userID cartID。否则可以删除别人的购物车
        List<CartVO> cartVOList = cartService.delete(UserFilter.currentUser.getId(), productId);
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 选中/不选中商品
     * @param productId 商品id
     * @param selected 0 1
     * @return
     */
    @PostMapping("/select")
    public ApiRestResponse select(@NotNull  @RequestParam() Integer productId,@NotNull @RequestParam Integer selected) {
        // 不能传入userID cartID。否则可以删除别人的购物车
        // 1选中 0不选
        if (selected != 0 && selected != 1) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
        List<CartVO> cartVOList = cartService.selectOrNot(UserFilter.currentUser.getId(), productId, selected);
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 全选
     * @param selected
     * @return
     */
    @PostMapping("/selectAll")
    public ApiRestResponse selectAll(@RequestParam() Integer selected) {
        if (selected != 0 && selected != 1) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
        List<CartVO> cartVOList = cartService.selectAllOrNot(UserFilter.currentUser.getId(), selected);
        return ApiRestResponse.success(cartVOList);
    }
}
