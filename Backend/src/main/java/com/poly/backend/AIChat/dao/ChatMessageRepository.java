package com.poly.backend.AIChat.dao;

import com.poly.backend.AIChat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    // Hàm custom để lấy toàn bộ lịch sử chat của 1 phiên, sắp xếp theo thời gian cũ -> mới
    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(String sessionId);
}