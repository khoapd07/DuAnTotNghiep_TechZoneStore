package com.poly.backend.service;

import com.poly.backend.dto.DashboardStatsDTO;

public interface DashboardService {
    // Hàm tổng hợp tất cả số liệu cho các thẻ Card
    DashboardStatsDTO getStats();

    // Các hàm cho biểu đồ (nếu cần tách riêng)
    // List<RevenueTrendDTO> getRevenueTrend();
}