package com.lty.service;

import com.lty.vo.OrderReportVO;
import com.lty.vo.OrderStatisticsVO;
import com.lty.vo.TurnoverReportVO;
import com.lty.vo.UserReportVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author lty
 */
public interface ReportService {
    TurnoverReportVO turnoverStatics(LocalDate begin, LocalDate end);

    UserReportVO userStatistics(LocalDate begin, LocalDate end);

    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);
}
