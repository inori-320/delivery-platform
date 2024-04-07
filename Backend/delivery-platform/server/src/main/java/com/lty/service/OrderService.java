package com.lty.service;

import com.lty.dto.*;
import com.lty.result.PageResult;
import com.lty.vo.OrderPaymentVO;
import com.lty.vo.OrderSubmitVO;
import com.lty.vo.OrderVO;

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

    PageResult pageQuery(int pageNum, int pageSize, Integer status);

    OrderVO showDetail(Long id);

    void cancelOrder(Long id) throws Exception;

    void cancelOrder(OrdersCancelDTO ordersCancelDTO) throws Exception;

    void reOrder(Long id);

    void confirm(OrdersConfirmDTO confirmDTO);

    void reject(OrdersRejectionDTO rejectionDTO) throws Exception;
}
