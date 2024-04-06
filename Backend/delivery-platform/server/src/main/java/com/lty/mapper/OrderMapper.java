package com.lty.mapper;

import com.lty.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lty
 */
@Mapper
public interface OrderMapper {

    void insertOrder(Orders orders);
}
