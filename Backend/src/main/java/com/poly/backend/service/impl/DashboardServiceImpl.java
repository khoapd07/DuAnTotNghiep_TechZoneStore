package com.poly.backend.service.impl;

import com.poly.backend.dto.DashboardStatsDTO;
import com.poly.backend.dao.OrderDAO;
import com.poly.backend.dao.ProductDAO;
import com.poly.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    // Đổi tên biến cho đúng chuẩn DAO bạn đã đặt
    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;

    @Override
    public DashboardStatsDTO getStats() {
        Double totalRevenue = orderDAO.sumTotalRevenue();
        Long newOrders = orderDAO.countOrdersByStatusName("ĐANG XỬ LÝ");

        // SỬA TẠI ĐÂY: Gọi đúng tên hàm mới trong ProductDAO
        Long outOfStock = productDAO.countByStockQuantity(0);

        return DashboardStatsDTO.builder()
                .totalRevenue(totalRevenue != null ? totalRevenue : 0.0)
                .revenueGrowth(12.5)
                .newOrdersCount(newOrders)
                .orderGrowth(-8.2)
                .outOfStockCount(outOfStock)
                .build();
    }
}