package com.poly.backend.dto;

import lombok.Data;

@Data
public class OrderRequestDTO {
    // Không nhận productId hay totalMoney từ Frontend để bảo mật
    private String note;         // Ghi chú giao hàng (VD: Giao giờ hành chính)
    private String voucherCode;  // Mã giảm giá khách nhập (Nếu có)
    private String email;
}