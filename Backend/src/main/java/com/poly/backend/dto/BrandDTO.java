package com.poly.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDTO {

    // ID có thể null khi tạo mới, nhưng sẽ có giá trị khi update hoặc get chi tiết
    private Integer brandId;

    // Validation: Không được để trống (null hoặc chuỗi rỗng)
    @NotBlank(message = "Tên thương hiệu không được để trống")
    // Validation: Độ dài tối đa 100 ký tự (khớp với Database)
    @Size(max = 100, message = "Tên thương hiệu không được quá 100 ký tự")
    private String brandName;

}