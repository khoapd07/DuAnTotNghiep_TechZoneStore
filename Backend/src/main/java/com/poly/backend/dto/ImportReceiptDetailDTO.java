package com.poly.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class ImportReceiptDetailDTO {
    private String receiptCode;
    private String supplierName;
    private String receiptDate;
    private String employeeName;
    private Double totalValue;
    private String note;
    private List<ImportReceiptItemDTO> items;

    @Data
    public static class ImportReceiptItemDTO {
        private String image;
        private String name;
        private String code;

        // --- BỔ SUNG: Tên phân loại sản phẩm ---
        private String variantName;

        private Integer quantity;
        private Double importPrice;
        private Double lineTotal;
    }
}