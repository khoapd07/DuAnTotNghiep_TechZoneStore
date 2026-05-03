package com.poly.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ProductVariants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_id")
    private Integer variantId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    // ĐÃ THÊM: Mã SKU
    @Column(name = "sku_code", unique = true)
    private String skuCode;

    @Column(name = "color_name", nullable = false)
    private String colorName;

    // ĐÃ THÊM: Phân loại 2
    @Column(name = "option2_value")
    private String option2Value;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    // ĐÃ THÊM: Số lượng tồn kho theo biến thể
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @ElementCollection
    @CollectionTable(name = "variant_images", joinColumns = @JoinColumn(name = "variant_id"))
    @Column(name = "image_url") // SỬA LẠI THÀNH image_url NHƯ THẾ NÀY
    private List<String> imageUrls = new ArrayList<>();
}