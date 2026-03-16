package com.poly.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

    // UserID cũng chính là CustomerID
    private Integer userId;

    // --- CUSTOMER SPECIFIC FIELDS ---

    @Size(max = 255, message = "Địa chỉ giao hàng không được quá 255 ký tự")
    private String shippingAddress;

    @Min(value = 0, message = "Điểm tích lũy không được âm")
    private Integer loyaltyPoints;

    // --- USER FIELDS (Lấy từ bảng Users sang để hiển thị) ---
    // Service sẽ có nhiệm vụ map dữ liệu từ User entity sang các trường này

    private String fullName;

    private String email;

    private String phoneNumber;

    private String avatar;

    // Status của tài khoản (Active/Locked)
    private Boolean status;

    // --- CÁC TRƯỜNG THÊM MỚI ---
    private String username; // Dùng khi tạo mới
    private String password; // Dùng khi tạo mới
    private LocalDateTime createdAt; // Dùng để thống kê khách mới trong tháng
}