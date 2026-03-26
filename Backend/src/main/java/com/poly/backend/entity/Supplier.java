package com.poly.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "supplier_name", nullable = false)
    private String supplierName;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;
    private String address;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean status;
}