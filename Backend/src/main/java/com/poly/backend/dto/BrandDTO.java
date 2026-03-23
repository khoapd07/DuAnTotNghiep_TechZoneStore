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

    private Integer brandId;

    @NotBlank(message = "Tên thương hiệu không được để trống")
    @Size(max = 100, message = "Tên thương hiệu không được quá 100 ký tự")
    private String brandName;

    // MỚI THÊM: Các trường hứng dữ liệu thống kê
    private Long productCount;
    private Long totalStock;

    // CONSTRUCTOR THÔNG MINH CHO JPQL
    public BrandDTO(Integer brandId, String brandName, Number productCount, Number totalStock) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.productCount = productCount != null ? productCount.longValue() : 0L;
        this.totalStock = totalStock != null ? totalStock.longValue() : 0L;
    }
}