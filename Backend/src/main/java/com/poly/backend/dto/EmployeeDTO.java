package com.poly.backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    // UserID cũng chính là EmployeeID
    private Integer userId;

    // --- EMPLOYEE SPECIFIC FIELDS ---

    @NotBlank(message = "Mã nhân viên không được để trống")
    @Size(max = 20, message = "Mã nhân viên không được quá 20 ký tự")
    private String employeeCode;

    @NotNull(message = "Ngày vào làm không được để trống")
    @PastOrPresent(message = "Ngày vào làm không được lớn hơn ngày hiện tại")
    private LocalDate hireDate;

    @NotNull(message = "Lương không được để trống")
    @Min(value = 0, message = "Lương phải lớn hơn hoặc bằng 0")
    private BigDecimal salary;

    @Size(max = 50, message = "Tên phòng ban không được quá 50 ký tự")
    private String department;

    // --- USER FIELDS (Lấy từ bảng Users sang) ---
    // Service sẽ map dữ liệu từ User entity sang các trường này

    private String username; // Để hiển thị tài khoản đăng nhập

    private String fullName;

    private String email;

    private String phoneNumber;

    private Boolean gender;

    private String avatar;

    private Boolean status; // Trạng thái làm việc (Active/Locked)

}