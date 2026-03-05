package com.poly.backend.service;

import com.poly.backend.dto.ChartDataDTO;
import com.poly.backend.dto.DashboardStatsDTO;

import java.util.List;

public interface DashboardService {
    DashboardStatsDTO getStats();

    List<ChartDataDTO> getChartData7Days();
}