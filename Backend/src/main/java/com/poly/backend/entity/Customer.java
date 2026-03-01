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

//    @Id
//    @Column(name = "user_id")
//    private Integer userId;

//    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId // Báo cho Hibernate biết khóa chính của Customer cũng chính là khóa ngoại trỏ tới User
//    @JoinColumn(name = "user_id")
//    @ToString.Exclude // Tránh lỗi StackOverflow khi in ra log (nếu Entity User cũng map ngược lại)
//    @EqualsAndHashCode.Exclude
//    private User user;

    @Column(name = "shipping_address", columnDefinition = "NVARCHAR(255)")
    private String shippingAddress;

    @Column(name = "loyalty_points")
    @Builder.Default
    private Integer loyaltyPoints = 0;

}