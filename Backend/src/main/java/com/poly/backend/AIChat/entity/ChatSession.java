package com.poly.backend.AIChat.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "ChatSessions")
@Data
public class ChatSession {
    @Id
    @Column(name = "session_id")
    private String sessionId;

    // Lưu ID của khách (nếu đã đăng nhập trên web)
    @Column(name = "customer_id")
    private Integer customerId;

    // Lưu ID của nhân viên (khi giành quyền trả lời)
    @Column(name = "staff_id")
    private Integer staffId;

    private String status = "AI_HANDLING";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}