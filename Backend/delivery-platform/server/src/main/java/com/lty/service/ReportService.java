package com.lty.service;

import com.lty.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author lty
 */
public interface ReportService {
    TurnoverReportVO turnoverStatics(LocalDate begin, LocalDate end);

    UserReportVO userStatistics(LocalDate begin, LocalDate end);

    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);

    SalesTop10ReportVO topStatistics(LocalDate begin, LocalDate end);

    void exportBusinessData(HttpServletResponse response);
}
