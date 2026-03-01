package com.poly.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)//
@Entity
@Table(name = "Employees")
@PrimaryKeyJoinColumn(name = "user_id")
public class Employee extends User {

//    @Id
//    @Column(name = "user_id")
//    private Integer userId;

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