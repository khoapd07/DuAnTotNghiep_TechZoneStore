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
    private String orderCode;          // Mã đơn (VD: TZ-20260302-XXXX)
    private LocalDateTime orderDate;   // Ngày đặt
    private BigDecimal totalMoney;     // Tổng tiền hàng
    private BigDecimal discountAmount; // Tiền giảm (nếu áp mã)
    private BigDecimal finalAmount;    // Tiền thực trả
    private String note;
    private String statusName;         // Trạng thái (Pending, Shipping,...)

    private List<OrderDetailResponseDTO> orderDetails; // Danh sách các món đã mua
}