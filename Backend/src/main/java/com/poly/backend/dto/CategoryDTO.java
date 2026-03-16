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
public class CategoryDTO {

    private Integer categoryId;

    @NotBlank(message = "Tên loại sản phẩm không được để trống")
    @Size(max = 100, message = "Tên loại sản phẩm không được quá 100 ký tự")
    private String categoryName;

    private String attributes;

}