package com.lty.service;

import com.lty.dto.OrdersPaymentDTO;
import com.lty.dto.OrdersSubmitDTO;
import com.lty.vo.OrderPaymentVO;
import com.lty.vo.OrderSubmitVO;

/**
 * @author lty
 */
public interface OrderService {

    OrderSubmitVO submitOrder(OrdersSubmitDTO submitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);
}
