package com.poly.backend.dto;

import lombok.Data;

@Data
public class CartItemRequestDTO {
    private Integer productId;

    private Integer variantId;

    private Integer quantity; // Số lượng muốn thêm hoặc cập nhật
}