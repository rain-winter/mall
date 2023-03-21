package com.mall.service.impl;

import com.mall.common.Constant;
import com.mall.exception.MallException;
import com.mall.exception.MallExceptionEnum;
import com.mall.model.mapper.CartMapper;
import com.mall.model.mapper.ProductMapper;
import com.mall.model.pojo.Cart;
import com.mall.model.pojo.Product;
import com.mall.service.CartService;
import com.mall.model.vo.CartVO;
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
            cartMapper.insert(cart);
        } else {
            // 这个商品已经在购物车里，则数量相加
//            count = cart.getQuantity() + count;
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.CHECKED);
            cartMapper.updateById(cartNew);
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
            cartMapper.insert(cart);
        } else {
            // 这个商品已经在购物车里，则数量相加
//            count = cart.getQuantity() + count;
            System.out.println("99999999999999999999999999999");
            System.out.println(count);
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.CHECKED);
            cartMapper.updateById(cartNew);
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
        Product product = productMapper.selectById(productId);
        // 经过查询后 判断商品是否上架
        if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
            throw new MallException(MallExceptionEnum.NOT_SALE);
        }
        // 判断商品库存。用户购买数量是否大于库存
        if (count > product.getStock()) {
            throw new MallException(MallExceptionEnum.NOT_ENOUGH);
        }
    }

    /**
     * 删除购物车
     *
     * @param userId    用户id
     * @param productId 商品id
     * @return
     */
    @Override
    public List<CartVO> delete(Integer userId, Integer productId) {
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            // 这个商品不在购物车，所以无法删除
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        } else {
            // 这个商品已经在购物车，可以删除
            cartMapper.deleteById(cart.getId());
        }
        return this.list(userId);
    }

    /**
     * 选中、全选状态
     *
     * @param userId    用户id
     * @param productId 商品id
     * @param selected  是否已勾选：0代表未勾选，1代表已勾选
     * @return
     */
    @Override
    public List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected) {
        List<CartVO> cart = cartMapper.selectList(userId);
        if (cart == null) {
            // 这个商品不在购物车，所以无法选中
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        } else {
            // 这个商品已经在购物车，可以删除
            cartMapper.selectOrNot(userId, productId, selected);
        }
        return this.list(userId);
    }

    /**
     * 选中
     * @param userId 用户id
     * @param selected 1选中 0不选
     * @return 所有选中或者未选得列表
     */
    @Override
    public List<CartVO> selectAllOrNot(Integer userId, Integer selected) {
        cartMapper.selectOrNot(userId, null, selected);
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
