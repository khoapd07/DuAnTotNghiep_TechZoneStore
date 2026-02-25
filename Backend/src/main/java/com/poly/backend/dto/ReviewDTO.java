package com.poly.backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    private Integer reviewId;

    // --- INPUT DATA (Người dùng gửi lên) ---

    @NotNull(message = "Vui lòng chọn số sao đánh giá")
    @Min(value = 1, message = "Đánh giá thấp nhất là 1 sao")
    @Max(value = 5, message = "Đánh giá cao nhất là 5 sao")
    private Integer rating;

    @Size(max = 500, message = "Nội dung đánh giá không được quá 500 ký tự")
    private String comment; // Có thể để trống nếu khách chỉ chấm sao

    @NotNull(message = "Sản phẩm đánh giá không được để trống")
    private Integer productId;

    // ID người dùng (Thường lấy từ Token đăng nhập, nhưng vẫn khai báo để map dữ liệu)
    private Integer userId;

    // --- OUTPUT DATA (Hiển thị lên giao diện) ---

    private LocalDateTime reviewDate;

    // 1. Thông tin người đánh giá (Lấy từ bảng Users)
    private String userFullName;
    private String userAvatar; // Để hiện ảnh đại diện người bình luận

    // 2. Thông tin sản phẩm (Dùng cho trang "Đánh giá của tôi" để biết mình đã review món gì)
    private String productName;
    private String productImage;

}