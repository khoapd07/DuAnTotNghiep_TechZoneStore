package com.poly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;       // Chuỗi JWT (quan trọng nhất)
    private String type = "Bearer"; // Kiểu token chuẩn
    private Integer userId;     // Để frontend lưu lại dùng khi gọi API khác
    private String username;
    private String role;        // Để frontend phân quyền hiển thị menu

    // Constructor tiện lợi
    public AuthResponse(String token, Integer userId, String username, String role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
}