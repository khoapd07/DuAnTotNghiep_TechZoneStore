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

    private Long productCount;
    private Long totalStock;

    // Thêm constructor này để map chính xác dữ liệu từ JPA Query trả về
    public BrandDTO(Integer brandId, String brandName, Long productCount, Number totalStock) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.productCount = productCount != null ? productCount : 0L;
        // Tự động convert Integer/int từ Database sang Long
        this.totalStock = totalStock != null ? totalStock.longValue() : 0L;
    }
}