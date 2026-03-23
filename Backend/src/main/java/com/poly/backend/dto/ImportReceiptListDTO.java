package com.poly.backend.dto;

import lombok.Data;

@Data
public class ImportReceiptListDTO {
    private Integer id;
    private String code;
    private String date;
    private Double total;
    private String creatorInitials;
    private String creatorName;
    private String supplier;
    private String note;
}