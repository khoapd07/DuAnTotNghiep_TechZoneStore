package com.poly.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // MỚI THÊM: Import JsonIgnore
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List; // MỚI THÊM: Import List

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "category_name", columnDefinition = "NVARCHAR(100)", nullable = false)
    private String categoryName;

    // --- MỚI THÊM: Quan hệ 1-Nhiều với Product ---
    @JsonIgnore // Cực kỳ quan trọng: Tránh lỗi lặp vô tận (Category gọi Product -> Product gọi Category)
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;

    // Hàm đếm số lượng giờ đã hoạt động bình thường
    public Integer getProductCount() {
        return this.products != null ? this.products.size() : 0;
    }

    public Integer getTotalStock() {
        if (this.products == null || this.products.isEmpty()) {
            return 0;
        }
        return this.products.stream()
                .mapToInt(product -> product.getStockQuantity() != null ? product.getStockQuantity() : 0)
                .sum();
    }
}