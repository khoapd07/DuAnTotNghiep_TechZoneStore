package com.poly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartDataDTO {
    private String label;      // Tên nhãn hiển thị ở trục X (Ví dụ: "T2", "T3" hoặc "05/03")
    private Double revenue;    // Dữ liệu cho đường Xu hướng doanh thu
    private Long orderCount;   // Dữ liệu cho cột Phân bổ đơn hàng
}