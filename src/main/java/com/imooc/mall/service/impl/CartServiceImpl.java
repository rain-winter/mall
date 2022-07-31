package com.imooc.mall.service.impl;

import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CartMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.service.CartService;
import com.imooc.mall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CartMapper cartMapper;

    /**
     * 更新商品
     *
     * @param userId    用户id
     * @param productId 商品id
     * @param count     商品数量
     * @return
     */
    @Override
    public List<CartVO> update(Integer userId, Integer productId, Integer count) {
        validProduct(productId, count);
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            // 这个商品之前不在购物车里，需要新增记录
            cart = new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setSelected(Constant.Cart.CHECKED);
            cartMapper.insertSelective(cart);
        } else {
            // 这个商品已经在购物车里，则数量相加
            count = cart.getQuantity() + count;
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.CHECKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    /**
     * 添加到购物车
     *
     * @param userId    当前用户的 id
     * @param productId 用户选中的商品的 id
     * @param count     商品的数量
     * @return
     */
    @Override
    public List<CartVO> add(Integer userId, Integer productId, Integer count) {
        validProduct(productId, count);
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            // 这个商品之前不在购物车里，需要新增记录
            cart = new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setSelected(Constant.Cart.CHECKED);
            cartMapper.insertSelective(cart);
        } else {
            // 这个商品已经在购物车里，则数量相加
            count = cart.getQuantity() + count;
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.CHECKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    /**
     * 经过查询后 判断商品是否上架，用户购买数量是否大于库存
     *
     * @param productId 商品的id
     * @param count     商品的数量
     */
    private void validProduct(Integer productId, Integer count) {
        Product product = productMapper.selectByPrimaryKey(productId);
        // 经过查询后 判断商品是否上架
        if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE);
        }
        // 判断商品库存。用户购买数量是否大于库存
        if (count > product.getStock()) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
        }
    }

    /**
     * 删除购物车
     * @param userId 用户id
     * @param productId 商品id
     * @return
     */
    @Override
    public List<CartVO> delete(Integer userId, Integer productId) {
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            // 这个商品不在购物车，所以无法删除
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        } else {
            // 这个商品已经在购物车，可以删除
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        return this.list(userId);
    }


    @Override
    public List<CartVO> list(Integer userId) {
        List<CartVO> cartVOS = cartMapper.selectList(userId);
        // 从数据库查到数据并设置totalPrice
        for (int i = 0; i < cartVOS.size(); i++) {
            CartVO cartVO = cartVOS.get(i);
            cartVO.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity());
        }
        return cartVOS;
    }
}
