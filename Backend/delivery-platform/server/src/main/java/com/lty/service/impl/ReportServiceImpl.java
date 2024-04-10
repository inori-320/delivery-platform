package com.lty.service.impl;

import com.lty.dto.GoodsSalesDTO;
import com.lty.entity.Orders;
import com.lty.mapper.OrderMapper;
import com.lty.mapper.UserMapper;
import com.lty.service.ReportService;
import com.lty.service.WorkspaceService;
import com.lty.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @author lty
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkspaceService workspaceService;

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

    public SalesTop10ReportVO topStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginDay = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endDay = LocalDateTime.of(end, LocalTime.MAX);
        List<String> names = new ArrayList<>();
        List<Integer> nums = new ArrayList<>();
        List<GoodsSalesDTO> goodsSalesDTOS = orderMapper.getSalesTop10(beginDay, endDay);
        log.info("售卖货物：{}", goodsSalesDTOS.toString());
        goodsSalesDTOS.forEach(g -> {
            names.add(g.getName());
            nums.add(g.getNumber());
        });
        String nameList = StringUtils.join(names, ",");
        String numberList = StringUtils.join(nums, ",");
        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }

    public void exportBusinessData(HttpServletResponse response) {
        // 查询数据库，获取营业数据——查询最近30天的运营数据
        LocalDate beginDay = LocalDate.now().minusDays(30);
        LocalDate endDay = LocalDate.now().minusDays(1);
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(beginDay, LocalTime.MIN), LocalDateTime.of(endDay, LocalTime.MAX));

        // 通过POI将数据写入到Excel中
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            XSSFWorkbook excel = new XSSFWorkbook(resource);
            XSSFSheet sheet = excel.getSheet("Sheet1");
            // 填充起始时间
            sheet.getRow(1).getCell(1).setCellValue("时间："+ beginDay + "至" + endDay);
            // 填充概览数据
            sheet.getRow(3).getCell(2).setCellValue(businessData.getTurnover());
            sheet.getRow(3).getCell(4).setCellValue(businessData.getOrderCompletionRate());
            sheet.getRow(3).getCell(6).setCellValue(businessData.getNewUsers());
            sheet.getRow(4).getCell(2).setCellValue(businessData.getValidOrderCount());
            sheet.getRow(4).getCell(4).setCellValue(businessData.getUnitPrice());
            // 填充明细数据
            for(int i = 0; i < 30; i++){
                LocalDate date = beginDay.plusDays(i);
                BusinessDataVO tmpData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                sheet.getRow(7 + i).getCell(1).setCellValue(date.toString());
                sheet.getRow(7 + i).getCell(2).setCellValue(tmpData.getTurnover());
                sheet.getRow(7 + i).getCell(3).setCellValue(tmpData.getValidOrderCount());
                sheet.getRow(7 + i).getCell(4).setCellValue(tmpData.getOrderCompletionRate());
                sheet.getRow(7 + i).getCell(5).setCellValue(tmpData.getUnitPrice());
                sheet.getRow(7 + i).getCell(6).setCellValue(tmpData.getNewUsers());
            }
            // 通过输出流将Excel下载到客户端浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            excel.write(outputStream);
            // 关闭资源
            outputStream.close();
            excel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
