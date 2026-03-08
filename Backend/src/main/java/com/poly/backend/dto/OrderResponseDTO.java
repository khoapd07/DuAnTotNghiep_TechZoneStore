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
    private Integer statusId;
    private String orderCode;
    private LocalDateTime orderDate;
    private BigDecimal totalMoney;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String note;
    private String statusName;


    private String customerName;
    private String employeeName;
    private String shipperName;

    private String paymentMethod;
    private Boolean paymentStatus;

    private List<OrderDetailResponseDTO> orderDetails;
}