package com.poly.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "OrderStatus")
public class OrderStatus {

    @Id
    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "status_name", columnDefinition = "NVARCHAR(50)", nullable = false)
    private String statusName;

    @Column(name = "description", columnDefinition = "NVARCHAR(255)")
    private String description;

}