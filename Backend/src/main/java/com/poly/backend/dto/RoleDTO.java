package com.poly.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {

    // Vì ID không tự tăng (IDENTITY), nên bắt buộc phải có khi thêm mới
    @NotNull(message = "Mã vai trò không được để trống")
    private Integer roleId;

    @NotBlank(message = "Tên vai trò không được để trống")
    @Size(max = 50, message = "Tên vai trò không được quá 50 ký tự")
    private String roleName;

}