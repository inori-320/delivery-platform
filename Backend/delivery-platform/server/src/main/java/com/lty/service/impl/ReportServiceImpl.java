package com.lty.service.impl;

import com.lty.entity.Orders;
import com.lty.mapper.OrderMapper;
import com.lty.service.ReportService;
import com.lty.vo.TurnoverReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lty
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;

    public TurnoverReportVO trunoverStatics(LocalDate begin, LocalDate end) {
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
}
