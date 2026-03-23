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

    // MỚI THÊM: Các trường hứng dữ liệu thống kê
    private Long productCount;
    private Long totalStock;

    // CONSTRUCTOR THÔNG MINH CHO JPQL: Dùng Number để bao xài mọi kiểu số từ DB trả về
    public CategoryDTO(Integer categoryId, String categoryName, Number productCount, Number totalStock) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productCount = productCount != null ? productCount.longValue() : 0L;
        this.totalStock = totalStock != null ? totalStock.longValue() : 0L;
    }
}