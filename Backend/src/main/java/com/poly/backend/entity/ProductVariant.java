package com.poly.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // Liên kết n-1 về Product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore // Tránh lỗi đệ quy lặp vô tận khi parse JSON
    private Product product;

    @Column(name = "color_name", nullable = false)
    private String colorName;

    @Column(name = "image_url")
    private String imageUrl;
}