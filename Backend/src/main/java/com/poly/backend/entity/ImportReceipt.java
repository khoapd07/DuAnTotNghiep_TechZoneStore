package com.poly.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "ImportReceipts")
public class ImportReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    private Integer receiptId;

    @Column(name = "receipt_code", unique = true, nullable = false)
    private String receiptCode;

    @Column(name = "receipt_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiptDate;

    @Column(name = "total_money")
    private Double totalMoney;

    private String note;

    // Liên kết với bảng Users (Nhân viên lập phiếu)
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    // Liên kết với bảng Suppliers
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    // Danh sách chi tiết phiếu nhập
    @OneToMany(mappedBy = "importReceipt", cascade = CascadeType.ALL)
    private List<ImportReceiptDetail> details;
}