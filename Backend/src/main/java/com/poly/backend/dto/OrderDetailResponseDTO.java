package com.poly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponseDTO {
    private Integer productId;
    private Integer variantId;
    private String productName;
    private String colorName;
    private String option2Value;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal price;      // Giá tại thời điểm mua (không phải giá hiện tại)
    private BigDecimal subTotal;   // Thành tiền (quantity * price)
}