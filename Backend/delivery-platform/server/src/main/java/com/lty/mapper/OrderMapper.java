package com.lty.mapper;

import com.github.pagehelper.Page;
import com.lty.dto.GoodsSalesDTO;
import com.lty.dto.OrdersPageQueryDTO;
import com.lty.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.aspectj.weaver.ast.Or;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author lty
 */
@Mapper
public interface OrderMapper {

    void insertOrder(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    @Update("update orders set status = #{status},pay_status = #{payStatus} ,checkout_time = #{checkoutTime} where number = #{number}")
    void updateStatus(Orders orders);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    @Select("select sum(amount) from orders where order_time > #{begin} and order_time < #{end} and status = #{status}")
    Double sumByMap(Map<String, Object> map);

    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    Integer countByMap(Map<String, Object> map);

    @Select("select * from orders where order_time > #{begin} and order_time < #{end}")
    List<Orders> countOrder(Map<String, Object> map);

    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}
