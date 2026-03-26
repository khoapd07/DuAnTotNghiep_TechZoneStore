package com.poly.backend.dto;

import lombok.Data;

@Data
public class SupplierDTO {
    private Integer id;
    private String name;
    private String contact;
    private String phone;
    private String email;
    private String address;
    private String taxCode;
    private Boolean status;
}