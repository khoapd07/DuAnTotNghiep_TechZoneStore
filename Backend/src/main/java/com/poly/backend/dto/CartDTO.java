package com.poly.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {

    private Integer cartId;

    // --- INPUT DATA (Dữ liệu bắt buộc khi thêm vào giỏ) ---

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng sản phẩm phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Sản phẩm không được để trống")
    private Integer productId;

    // User ID thường lấy từ Token (Security), nhưng để đây cho trường hợp Admin xem giỏ hàng
    private Integer userId;

    // --- OUTPUT DATA (Dữ liệu hiển thị lên Frontend) ---
    // Các field này Service sẽ tự fill vào, Frontend chỉ việc hiển thị

    private String productName;

    private String productImage;

    private BigDecimal productPrice; // Giá tại thời điểm hiện tại của sản phẩm

    private BigDecimal productSalePrice; // Giá khuyến mãi (nếu có)

    // Field tính toán: Tổng tiền = (Giá * Số lượng)
    private BigDecimal totalPrice;

    private LocalDateTime updatedAt;
}