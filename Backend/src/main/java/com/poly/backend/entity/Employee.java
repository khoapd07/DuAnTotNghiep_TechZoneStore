package com.poly.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Employees")
public class Employee {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Giống bảng Customer, khóa chính cũng là khóa ngoại trỏ tới User
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @Column(name = "employee_code", length = 20, unique = true)
    private String employeeCode;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "salary", precision = 18, scale = 2)
    private BigDecimal salary;

    @Column(name = "department", columnDefinition = "NVARCHAR(50)")
    private String department;

}