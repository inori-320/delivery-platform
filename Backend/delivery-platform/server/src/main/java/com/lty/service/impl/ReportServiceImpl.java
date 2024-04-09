package com.lty.service.impl;

import com.lty.entity.Orders;
import com.lty.mapper.OrderMapper;
import com.lty.mapper.UserMapper;
import com.lty.service.ReportService;
import com.lty.vo.OrderReportVO;
import com.lty.vo.OrderStatisticsVO;
import com.lty.vo.TurnoverReportVO;
import com.lty.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @author lty
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    public TurnoverReportVO turnoverStatics(LocalDate begin, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        List<Double> turnovers = new ArrayList<>();
        LocalDate date = begin;
        while(true){
            dates.add(date);
            if(date.equals(end)) break;
            date = date.plusDays(1);
        }
        dates.forEach(d -> {
            LocalDateTime beginDay = LocalDateTime.of(d, LocalTime.MIN);
            LocalDateTime endDay = LocalDateTime.of(d, LocalTime.MAX);
            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginDay);
            map.put("end", endDay);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            turnovers.add(turnover == null ? 0.0 : turnover);
        });
        String dateList = StringUtils.join(dates, ",");
        String turnoverList = StringUtils.join(turnovers, ",");
        return TurnoverReportVO.builder().dateList(dateList).turnoverList(turnoverList).build();
    }

    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        List<Integer> users = new ArrayList<>();
        List<Integer> newUsers = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        LocalDate date = begin.plusDays(-1);
        LocalDateTime beginDay = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endDay = LocalDateTime.of(date, LocalTime.MAX);
        map.put("begin", beginDay);
        map.put("end", endDay);
        Integer preUser = userMapper.countUser(map);
        date = begin;
        while(true){
            dates.add(date);
            if(date.equals(end)) break;
            date = date.plusDays(1);
        }
        for(LocalDate d: dates){
            beginDay = LocalDateTime.of(d, LocalTime.MIN);
            endDay = LocalDateTime.of(d, LocalTime.MAX);
            map.clear();
            map.put("begin", beginDay);
            map.put("end", endDay);
            Integer user = userMapper.countUser(map);
            Integer newUser = user - preUser;
            preUser = user;
            users.add(user);
            newUsers.add(newUser);
        }

        String dateList = StringUtils.join(dates, ",");
        String newUserList = StringUtils.join(newUsers, ",");
        String totalUserList = StringUtils.join(users, ",");
        return UserReportVO.builder().dateList(dateList).newUserList(newUserList).totalUserList(totalUserList).build();
    }

    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        List<Integer> orders = new ArrayList<>();
        List<Integer> validOrders = new ArrayList<>();
        LocalDate date = begin;
        while(true){
            dates.add(date);
            if(date.equals(end)) break;
            date = date.plusDays(1);
        }
        List<Orders> ordersList;
        int totalOrderCount = 0;
        int validOrderCount = 0;
        for(LocalDate d: dates){
            LocalDateTime beginDay = LocalDateTime.of(d, LocalTime.MIN);
            LocalDateTime endDay = LocalDateTime.of(d, LocalTime.MAX);
            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginDay);
            map.put("end", endDay);
            ordersList = orderMapper.countOrder(map);
            orders.add(ordersList.size());
            totalOrderCount += ordersList.size();
            int cnt = 0;
            for (Orders order : ordersList) {
                if(order.getStatus().equals(Orders.COMPLETED)){
                    validOrderCount ++;
                    cnt ++;
                }
            }
            validOrders.add(cnt);
        }
        String dateList = StringUtils.join(dates, ",");
        String orderCountList = StringUtils.join(orders, ",");
        String validOrderCountList = StringUtils.join(validOrders, ",");
        Double orderCompletionRate = validOrderCount == totalOrderCount ? 1 : (double)validOrderCount / (double)totalOrderCount;
        return OrderReportVO.builder()
                .dateList(dateList)
                .orderCountList(orderCountList)
                .orderCompletionRate(orderCompletionRate)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .validOrderCountList(validOrderCountList)
                .build();
    }
}
