package com.poly.backend.AIChat.dao;

import com.poly.backend.AIChat.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, String> {
    // Spring Data JPA đã hỗ trợ sẵn các hàm findById, save, delete...
    List<ChatSession> findAllByOrderByUpdatedAtDesc();

}