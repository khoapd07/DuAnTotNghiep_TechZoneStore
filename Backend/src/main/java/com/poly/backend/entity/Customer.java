package com.poly.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)//gọi equals của cha
@Entity
@Table(name = "Customers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends User {



    @Column(name = "shipping_address", columnDefinition = "NVARCHAR(255)")
    private String shippingAddress;

    @Column(name = "loyalty_points")
    @Builder.Default
    private Integer loyaltyPoints = 0;

}