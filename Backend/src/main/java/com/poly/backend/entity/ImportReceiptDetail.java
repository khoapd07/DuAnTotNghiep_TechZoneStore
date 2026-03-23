package com.poly.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "ImportReceiptDetails")
public class ImportReceiptDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Integer detailId;

    @ManyToOne
    @JoinColumn(name = "receipt_id", nullable = false)
    @JsonIgnore // Tránh lỗi vòng lặp vô tận khi parse JSON
    private ImportReceipt importReceipt;

    // Liên kết với bảng Products đã có
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // --- BỔ SUNG: Liên kết với bảng Phân loại (ProductVariants) ---
    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "import_price", nullable = false)
    private Double importPrice;

    @Column(name = "line_total", nullable = false)
    private Double lineTotal;
}