package com.poly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private Integer orderId;
    private String orderCode;
    private LocalDateTime orderDate;
    private BigDecimal totalMoney;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String note;
    private String statusName;

    // THÊM DÒNG NÀY ĐỂ TRẢ TÊN KHÁCH HÀNG VỀ CHO FRONTEND
    private String customerName;

    private List<OrderDetailResponseDTO> orderDetails;
}