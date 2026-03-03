package com.poly.backend.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class GuestCartItemDTO {
    private Integer productId;
    private Integer quantity;
    private BigDecimal price; // Giá lúc thêm vào giỏ
}