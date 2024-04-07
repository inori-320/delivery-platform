package com.lty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lty.constant.MessageConstant;
import com.lty.context.BaseContext;
import com.lty.dto.OrdersPageQueryDTO;
import com.lty.dto.OrdersPaymentDTO;
import com.lty.dto.OrdersSubmitDTO;
import com.lty.entity.*;
import com.lty.exception.AddressBookBusinessException;
import com.lty.exception.OrderBusinessException;
import com.lty.exception.ShoppingCartBusinessException;
import com.lty.mapper.*;
import com.lty.result.PageResult;
import com.lty.service.OrderService;
import com.lty.utils.WeChatPayUtil;
import com.lty.vo.OrderPaymentVO;
import com.lty.vo.OrderSubmitVO;
import com.lty.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lty
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO submitDTO) {
        // 先校验是否符合下单要求
        AddressBook addressBook = addressBookMapper.getById(submitDTO.getAddressBookId());
        if(addressBook == null) throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        ShoppingCart shoppingCart = new ShoppingCart();
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> carts = shoppingCartMapper.shopIsExist(shoppingCart);
        if(carts == null || carts.isEmpty()) throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);

        // 向订单表中插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(submitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setUserId(userId);
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(addressBook.getProvinceName()
                + addressBook.getCityName()
                + addressBook.getDistrictName()
                + addressBook.getDetail());
        orderMapper.insertOrder(orders);
        // 向订单详情表中插入多条数据
        List<OrderDetail> orderDetails = new ArrayList<>();
        carts.forEach(cart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetails.add(orderDetail);
        });
        orderDetailMapper.insertBatch(orderDetails);
        // 清空购物车
        shoppingCartMapper.cleanCartByUserId(userId);

        return OrderSubmitVO.builder().
                id(orders.getId()).
                orderNumber(orders.getNumber()).
                orderTime(orders.getOrderTime())
                .orderAmount(orders.getAmount())
                .build();
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);
/*
        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }
*/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "ORDERPAID");
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));
        //为替代微信支付成功后的数据库订单状态更新，多定义一个方法进行修改
        Orders orders = new Orders();
        orders.setStatus(Orders.TO_BE_CONFIRMED);
        orders.setPayStatus(Orders.PAID);
        orders.setNumber(ordersPaymentDTO.getOrderNumber());
        orderMapper.update(orders);
        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    public PageResult pageQuery(int pageNum, int pageSize, Integer status) {
        PageHelper.startPage(pageNum, pageSize);
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        List<OrderVO> orders = new ArrayList<>();
        if(page != null && page.getTotal() > 0){
            for (Orders o: page){
                Long orderId = o.getId();
                OrderVO orderVO = new OrderVO();
                List<OrderDetail> list = orderDetailMapper.getByOrderId(orderId);
                BeanUtils.copyProperties(o, orderVO);
                orderVO.setOrderDetailList(list);
                orders.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), orders);
    }

    public OrderVO showDetail(Long id) {
        Orders order = orderMapper.getById(id);
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    public void cancelOrder(Long id) throws Exception {
        Orders order = orderMapper.getById(id);
        if(order == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if(order.getStatus() > 2){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        orders.setId(id);
        if(order.getStatus().equals(Orders.TO_BE_CONFIRMED)){
        /*
            //调用微信支付退款接口
            weChatPayUtil.refund(
                    order.getNumber(), //商户订单号
                    order.getNumber(), //商户退款单号
                    new BigDecimal(0.01),//退款金额，单位 元
                    new BigDecimal(0.01));//原订单金额
         */
            orders.setPayStatus(Orders.REFUND);
        }
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    public void reOrder(Long id) {
        Long userId = BaseContext.getCurrentId();
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);
        List<ShoppingCart> shoppingCarts = orderDetails.stream().map(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(x, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).toList();
        shoppingCartMapper.insertBatch(shoppingCarts);
    }
}