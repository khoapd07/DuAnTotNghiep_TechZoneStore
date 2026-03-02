package com.poly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDTO {
    private Integer cartId;
    private Integer userId;
    private List<CartItemResponseDTO> items;
    private BigDecimal cartTotal; // Tổng tiền toàn bộ giỏ hàng
}