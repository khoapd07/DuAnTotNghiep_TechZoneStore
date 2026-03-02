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
public class CartItemResponseDTO {
    private Integer cartItemId;
    private Integer productId;
    private String productName;
    private String imageUrl;
    private BigDecimal price;      // Giá gốc
    private BigDecimal salePrice;  // Giá khuyến mãi (nếu có)
    private Integer quantity;      // Số lượng trong giỏ
    private BigDecimal subTotal;   // Thành tiền của món này (Giá áp dụng * số lượng)
}