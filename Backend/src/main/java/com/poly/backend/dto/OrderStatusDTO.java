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
public class OrderStatusDTO {

    // Vì ID không tự tăng (IDENTITY), nên khi thêm mới bắt buộc phải nhập ID
    @NotNull(message = "Mã trạng thái không được để trống")
    private Integer statusId;

    @NotBlank(message = "Tên trạng thái không được để trống")
    @Size(max = 50, message = "Tên trạng thái không được quá 50 ký tự")
    private String statusName;

    @Size(max = 255, message = "Mô tả không được quá 255 ký tự")
    private String description;

}