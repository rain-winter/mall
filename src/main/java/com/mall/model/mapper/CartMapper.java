package com.mall.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.model.pojo.Cart;
import com.mall.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper extends BaseMapper<Cart> {
    //    根据用户id和商品id查找商品
    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<CartVO> selectList(@Param("userId") Integer userId);

    // 更新选中状态
    Integer selectOrNot(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("selected") Integer selected);
}
