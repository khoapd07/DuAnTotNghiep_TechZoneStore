package com.poly.backend.dto;

import com.poly.backend.entity.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Integer productId;
    private String name;
    private BigDecimal price;
    private BigDecimal salePrice;

    // Vẫn giữ biến này ở DTO để Frontend hiển thị "Tổng tồn kho" không bị lỗi
    private Integer stockQuantity;

    private String description;
    private String imageUrl;
    private Boolean active;
    private Date createdAt;
    private Integer categoryId;
    private String categoryName;
    private Integer brandId;
    private String brandName;

    private String attributes;
    // ĐÃ XÓA: capacities

    private List<ProductVariant> variants;
}