package com.poly.backend.dto;

import jakarta.validation.constraints.*;
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
public class VoucherDTO {

    private Integer voucherId;

    @NotBlank(message = "Mã giảm giá không được để trống")
    @Size(max = 20, message = "Mã giảm giá không được quá 20 ký tự")
    // Note: Thường mã voucher viết hoa không dấu, Service nên upperCase nó trước khi lưu
    private String code;

    @NotBlank(message = "Tên chương trình giảm giá không được để trống")
    @Size(max = 100, message = "Tên chương trình không được quá 100 ký tự")
    private String name;

    @NotNull(message = "Số tiền giảm không được để trống")
    @Min(value = 0, message = "Số tiền giảm phải lớn hơn hoặc bằng 0")
    private BigDecimal discountAmount;

    @Min(value = 0, message = "Giá trị đơn tối thiểu phải lớn hơn hoặc bằng 0")
    private BigDecimal minOrderValue; // Có thể null (nếu logic service cho phép), hoặc default = 0

    @NotNull(message = "Số lượng mã không được để trống")
    @Min(value = 1, message = "Số lượng mã phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDateTime startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDateTime endDate;

    private Boolean status; // Active/Inactive

}