package com.lty.service;

import com.lty.dto.OrdersSubmitDTO;
import com.lty.vo.OrderSubmitVO;

/**
 * @author lty
 */
public interface OrderService {

    OrderSubmitVO submitOrder(OrdersSubmitDTO submitDTO);
}
