package com.poly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardStatsDTO {

    // Thẻ 1: Tổng doanh thu (e.g., 150.000.000₫)
    private Double totalRevenue;
    private Double revenueGrowth; // Tỉ lệ tăng trưởng doanh thu (e.g., 12.5)

    // Thẻ 2: Đơn hàng mới (e.g., 42 Đơn)
    private Long newOrdersCount;
    private Double orderGrowth; // Tỉ lệ tăng/giảm đơn hàng (e.g., -8.2)

    // Thẻ 3: Sản phẩm sắp hết hàng hoặc hết hàng (e.g., 15 Items)
    private Long outOfStockCount;

    // Bạn có thể thêm trường này nếu muốn hiển thị tổng số khách hàng mới
    private Long totalCustomers;
}