package com.poly.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class UserDTO {

    private Integer userId;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(max = 50, message = "Tên đăng nhập không được quá 50 ký tự")
    private String username;

    // --- QUAN TRỌNG: BẢO MẬT PASSWORD ---
    // @JsonProperty(access = WRITE_ONLY):
    // - Khi Frontend gửi lên (JSON -> Java): Nhận bình thường để lưu/đăng nhập.
    // - Khi Backend trả về (Java -> JSON): Tự động ẩn field này đi.
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 100, message = "Email không được quá 100 ký tự")
    private String email;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 100, message = "Họ và tên không được quá 100 ký tự")
    private String fullName;

    // Validate số điện thoại Việt Nam đơn giản (10 số, bắt đầu bằng 0)
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    private Boolean gender; // true: Nam, false: Nữ

    @Size(max = 255, message = "Địa chỉ không được quá 255 ký tự")
    private String address;

    private String avatar;

    private Boolean status; // 1: Active, 0: Locked

    // Token reset pass tuyệt đối không trả về qua API user thông thường
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String resetPasswordToken;

    private LocalDateTime createdAt;

    // --- RELATIONSHIP (ROLE) ---

    // Input: Dùng khi tạo user mới (mặc định là User=0, nhưng Admin có thể tạo Staff=1)
    private Integer roleId;

    // Output: Để hiển thị xem user này quyền gì (Admin/Staff/User)
    private String roleName;

}