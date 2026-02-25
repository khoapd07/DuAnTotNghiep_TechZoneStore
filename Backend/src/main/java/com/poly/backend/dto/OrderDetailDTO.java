package com.poly.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {

    private Integer orderDetailId;

    private Integer orderId; // ID của hóa đơn cha

    // --- INPUT DATA (Dữ liệu Frontend gửi lên khi đặt hàng) ---

    @NotNull(message = "Sản phẩm không được để trống")
    private Integer productId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng mua phải lớn hơn 0")
    private Integer quantity;

    // --- OUTPUT DATA (Dữ liệu trả về cho Frontend hiển thị) ---

    // 1. Giá tại thời điểm mua (QUAN TRỌNG: Đây là giá đã chốt, không đổi dù giá gốc có tăng/giảm)
    private BigDecimal price;

    // 2. Thành tiền của riêng dòng này (price * quantity) - Backend tự tính
    private BigDecimal totalPrice;

    // 3. Thông tin sản phẩm (Lấy từ bảng Products sang)
    private String productName;
    private String productImage;

}