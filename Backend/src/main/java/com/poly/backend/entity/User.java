package com.poly.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "full_name", columnDefinition = "NVARCHAR(100)", nullable = false)
    private String fullName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "gender")
    private Boolean gender; // true: Nam, false: Nữ

    @Column(name = "address", columnDefinition = "NVARCHAR(255)")
    private String address;

    @Column(name = "avatar", columnDefinition = "NVARCHAR(255)")
    private String avatar;

    @Column(name = "status")
    @Builder.Default
    private Boolean status = true; // 1: Active, 0: Locked

    @Column(name = "reset_password_token", length = 255)
    private String resetPasswordToken;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // --- RELATIONSHIPS ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // THÊM MỚI: Liên kết 1-1 với Giỏ hàng
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Chặn lỗi lặp JSON khi gọi API
    private Cart cart;
}