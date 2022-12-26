package com.mall.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.mall.common.Constant;
import com.mall.exception.MallException;
import com.mall.exception.MallExceptionEnum;
import com.mall.filter.UserFilter;
import com.mall.model.mapper.CartMapper;
import com.mall.model.mapper.OrderItemMapper;
import com.mall.model.mapper.OrderMapper;
import com.mall.model.mapper.ProductMapper;
import com.mall.model.pojo.Order;
import com.mall.model.pojo.OrderItem;
import com.mall.model.pojo.Product;
import com.mall.model.request.CreateOrderReq;
import com.mall.model.vo.CartVO;
import com.mall.model.vo.OrderItemVO;
import com.mall.model.vo.OrderVO;
import com.mall.service.CartService;
import com.mall.service.OrderService;
import com.mall.service.UserService;
import com.mall.utils.OrderCodeFactory;
import com.mall.utils.QrcodeGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述: 订单 Service实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CartService cartService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    UserService userService;

    @Value("${file.upload.ip}")
    String ip;

    /**
     * 创建订单
     *
     * @param createOrderReq 收货人姓名、手机号、地址、运费、支付类型
     * @return 订单编号
     */
    // 数据库事务 有任何异常都会回滚，不插入数据库
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(CreateOrderReq createOrderReq) {
        // 1. 拿到用户ID
        Integer userId = UserFilter.currentUser.getId();
        // 2. 从购物车查找已经勾选的商品
        List<CartVO> cartVOList = cartService.list(userId); // 选中的 商品列表
        ArrayList<CartVO> cartVOListTmp = new ArrayList<>();
        for (CartVO cartVO : cartVOList) {
            System.out.println(cartVO);
            // 筛选出选中的商品
            if (cartVO.getSelected().equals(Constant.Cart.CHECKED)) {
                cartVOListTmp.add(cartVO);
            }
        }
        cartVOList = cartVOListTmp;
        // 3.如果购物车已勾选为空，则报错
        if (CollectionUtils.isEmpty(cartVOList)) {
            throw new MallException(MallExceptionEnum.CART_EMPTY);
        }
        // 4.判断商品是否存在，上下架状态、库存
        validSaleStatusAndStock(cartVOList);
        // 5.把购物车对象转为订单item对象
        List<OrderItem> orderItemList = cartVOListToOrderItemList(cartVOList);
        // 扣库存
        for (OrderItem orderItem : orderItemList) {
            // 拿到该商品
            Product product = productMapper.selectById(orderItem.getProductId());
            int stock = product.getStock() - orderItem.getQuantity();
            if (stock < 0) {
                throw new MallException(MallExceptionEnum.NAME_NOT_NULL);
            }
            product.setStock(stock);
            productMapper.updateById(product);
        }
        // 把购物车中的已勾选商品删除
        cleanCart(cartVOList);

        // 生成订单号，有独立规则
        Order order = new Order();
//        生成订单号
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(totalPrice(orderItemList));
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAID.getCode());
        order.setPaymentType(0);
        order.setPaymentType(1);
        // 插入到order表
        orderMapper.insert(order);
        // 循环保存每个商品到order_item表
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(orderNo);
            orderItemMapper.insert(orderItem);
        }
        return orderNo;
    }

    /**
     * 求购物车所有物品得总价
     *
     * @param orderItemList 订单子项
     */
    private Integer totalPrice(List<OrderItem> orderItemList) {
        Integer totalPrice = 0;
        for (OrderItem orderItem : orderItemList) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    // 清空购物车
    private void cleanCart(List<CartVO> cartVOList) {
        for (CartVO cartVO : cartVOList) {
            cartMapper.deleteById(cartVO.getId());
        }
    }

    /**
     * 生成订单子项
     *
     * @param cartVOList 用户选中的商品列表
     */
    private List<OrderItem> cartVOListToOrderItemList(List<CartVO> cartVOList) {
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        for (CartVO cartVO : cartVOList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            // 记录商品快照信息
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    /**
     * 判断选中的商品 库存、数量、上下架是否合规
     *
     * @param cartVOList 、
     */
    public void validSaleStatusAndStock(List<CartVO> cartVOList) {
        for (CartVO cartVO : cartVOList) {
            Product product = productMapper.selectById(cartVO.getProductId());
            // 判断商品是否存在，商品是否上架
            if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
                throw new MallException(MallExceptionEnum.NOT_SALE);
            }
            // 判断商品库存
            if (cartVO.getQuantity() > product.getStock()) {
                throw new MallException(MallExceptionEnum.NOT_ENOUGH);
            }
        }
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * 查看订单详情
     *
     * @param orderNo 订单编号
     * @return
     */
    @Override
    public OrderVO detail(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        //  订单不存在
        if (order == null) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        // 订单存在，判断所属
        Integer userId = UserFilter.currentUser.getId();
        OrderVO orderVO = null;
        if (userService.checkAdminRole(UserFilter.currentUser)) {
             orderVO = getOrderVO(order);
        }else if(!order.getUserId().equals(userId)){
            throw new MallException(MallExceptionEnum.NOT_YOUR_ORDER);
        }
//        if (!order.getUserId().equals(userId)) {
//            throw new MallException(MallExceptionEnum.NOT_YOUR_ORDER);
//        }
//        OrderVO orderVO = getOrderVO(order);
        return orderVO;
    }

    /**
     * 返回一个OrderVO对象
     *
     * @param order 一个订单
     * @return OrderVO
     */
    private OrderVO getOrderVO(Order order) {
        OrderVO orderVO = new OrderVO(); // 里面包含了订单列表和订单状态
        BeanUtils.copyProperties(order, orderVO);
        // 获取订单对应的orderItemVOList
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
        List<OrderItemVO> orderItemVOList = new ArrayList();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(orderVO.getOrderStatus()).getValue());
        return orderVO;
    }

    //--------------------------------------------------------------------------------------

    /**
     * 给顾客看的分页列表
     *
     * @param pageNum  第几页
     * @param pageSize 一页几条
     * @return pageinfo
     */
    @Override
    public PageInfo listForCustomer(Integer pageNum, Integer pageSize) {
        Integer userId = UserFilter.currentUser.getId();
        List<Order> orderList = orderMapper.selectForCustomer(userId);
        PageHelper.startPage(pageNum, pageSize);
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    private List<OrderVO> orderListToOrderVOList(List<Order> orderList) {
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVO orderVO = getOrderVO(order);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    // ----------------------------------------------------------------------------------------

    /**
     * 取消订单
     *
     * @param orderNo 订单号
     */
    @Override
    public void cancel(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        // 查不到订单，报错
        if (order == null) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        // 验证用户身份
        Integer userId = UserFilter.currentUser.getId();
        if (!order.getUserId().equals(userId)) {
            throw new MallException(MallExceptionEnum.NOT_YOUR_ORDER);
        }
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAID.getCode())) {
            order.setOrderStatus(Constant.OrderStatusEnum.CANCELED.getCode());
            order.setEndTime(new Date());
            orderMapper.deleteById(order);
        } else {
            throw new MallException(MallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    /**
     * 生成二维码
     *
     * @param orderNo 订单号
     */
    @Override
    public String qrcode(String orderNo) {
        Boolean orderIsExist = orderIsExist(orderNo);
        if (!orderIsExist) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        // 这个ip知识和简单的网络环境（只是用wifi、4G）像是蓝牙就不太准确
        // 这个ip是本机局域网。这个ip可以用手机扫码
//        try {
//            ip = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            throw new RuntimeException(e);
//        }

        String address = ip + ":" + request.getLocalPort();
        String payUrl = "http://" + address + "/pay?orderNo=" + orderNo; // http://127.0.0.1/8083/pay?orderNo=orderNo
        String pngAddress;
        try {
            QrcodeGenerator.generateQRCodeImage(payUrl, 350, 350, Constant.FILE_UPLOAD_DIR + orderNo + ".png");
            pngAddress = "http://" + address + "/images/" + orderNo + ".png"; //  // http://127.0.0.1//images/orderNo.png
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
        return pngAddress;
    }

    private Boolean orderIsExist(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        return order != null;
    }

    // 管理员看的分页
    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectAllForAdmin();
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    /**
     * 支付接口
     *
     * @param orderNo 订单号
     */
    @Override
    public void pay(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        if (order.getOrderStatus() == Constant.OrderStatusEnum.NOT_PAID.getCode()) {
            order.setOrderStatus(Constant.OrderStatusEnum.PAID.getCode()); // 更改支付code为 “已付款”
            order.setPayTime(new Date()); // 设置支付时间
            orderMapper.updateById(order);
        } else {
            throw new MallException(MallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    @Override
    public void deliver(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        if (order.getOrderStatus() == Constant.OrderStatusEnum.PAID.getCode()) {
            order.setOrderStatus(Constant.OrderStatusEnum.DELIVERED.getCode()); // 更改支付code为 “已付款”
            order.setDeliveryTime(new Date()); // 设置已发货时间
            orderMapper.updateById(order);
        } else {
            throw new MallException(MallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    @Override
    public void finish(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        // 校验  普通用户 订单所属
        if (!userService.checkAdminRole(UserFilter.currentUser)
                && !order.getUserId().equals(UserFilter.currentUser.getId())) {
            throw new MallException(MallExceptionEnum.NOT_YOUR_ORDER);
        }
        // 发货后可以完结订单 管理员可以直接完结订单
        if (order.getOrderStatus() == Constant.OrderStatusEnum.DELIVERED.getCode()) {
            order.setOrderStatus(Constant.OrderStatusEnum.FINISHED.getCode()); // 更改支付code为 “已付款”
            order.setEndTime(new Date()); // 设置已发货时间
            orderMapper.updateById(order);
        } else {
            throw new MallException(MallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }
}

