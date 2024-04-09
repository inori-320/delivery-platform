package com.lty.service;

import com.lty.vo.TurnoverReportVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author lty
 */
public interface ReportService {
    TurnoverReportVO trunoverStatics(LocalDate begin, LocalDate end);
}
