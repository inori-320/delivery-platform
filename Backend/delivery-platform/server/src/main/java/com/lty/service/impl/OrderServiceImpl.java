package com.lty.service.impl;

import com.lty.constant.MessageConstant;
import com.lty.context.BaseContext;
import com.lty.dto.OrdersSubmitDTO;
import com.lty.entity.AddressBook;
import com.lty.entity.OrderDetail;
import com.lty.entity.Orders;
import com.lty.entity.ShoppingCart;
import com.lty.exception.AddressBookBusinessException;
import com.lty.exception.ShoppingCartBusinessException;
import com.lty.mapper.AddressBookMapper;
import com.lty.mapper.OrderDetailMapper;
import com.lty.mapper.OrderMapper;
import com.lty.mapper.ShoppingCartMapper;
import com.lty.service.OrderService;
import com.lty.vo.OrderSubmitVO;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
}
