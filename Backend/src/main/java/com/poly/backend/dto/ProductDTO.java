package com.poly.backend.dto;

import com.poly.backend.entity.ProductVariant;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Integer productId;


    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm không được quá 255 ký tự")
    private String name;

    @NotNull(message = "Giá gốc không được để trống")
    @Min(value = 0, message = "Giá gốc phải lớn hơn hoặc bằng 0")
    private BigDecimal price;

    @Min(value = 0, message = "Giá khuyến mãi phải lớn hơn hoặc bằng 0")
    private BigDecimal salePrice; // Có thể null

    @NotNull(message = "Số lượng tồn kho không được để trống")
    @Min(value = 0, message = "Số lượng tồn kho không được âm")
    private Integer stockQuantity;


    private String description;

    private String imageUrl;

    private Boolean active; // Trạng thái kinh doanh (Còn bán/Ngừng bán)

    private LocalDateTime createdAt; // Chỉ để hiển thị (Output)

    // --- RELATIONSHIP INPUT (Dùng khi Thêm/Sửa - Gửi ID lên) ---

    @NotNull(message = "Vui lòng chọn danh mục sản phẩm")
    private Integer categoryId;

    @NotNull(message = "Vui lòng chọn thương hiệu")
    private Integer brandId;

    // --- RELATIONSHIP OUTPUT (Dùng khi Hiển thị - Server trả Name về) ---

    private String categoryName;

    private String brandName;

    private List<ProductVariant> variants;

}