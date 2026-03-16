package com.poly.backend.AIChat.service.impl;

import com.poly.backend.AIChat.dao.ChatSessionRepository;
import com.poly.backend.AIChat.entity.ChatSession;
import com.poly.backend.AIChat.service.ChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatSessionServiceImpl implements ChatSessionService {

    @Autowired
    private ChatSessionRepository sessionRepo;

    @Override
    public ChatSession createOrGetSession(String sessionId, Integer customerId) {
        return sessionRepo.findById(sessionId).orElseGet(() -> {
            ChatSession newSession = new ChatSession();
            newSession.setSessionId(sessionId);
            newSession.setCustomerId(customerId);
            newSession.setStatus("AI_HANDLING"); // Mặc định AI sẽ tiếp khách trước
            return sessionRepo.save(newSession);
        });
    }

    @Override
    public ChatSession updateStatus(String sessionId, String status, Integer staffId) {
        ChatSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiên chat này!"));

        session.setStatus(status);
        if (staffId != null) {
            session.setStaffId(staffId); // Ghi nhận ID nhân viên đang hỗ trợ
        }

        return sessionRepo.save(session);
    }

    @Override
    public ChatSession getSession(String sessionId) {
        return sessionRepo.findById(sessionId).orElse(null);
    }

    @Override
    public List<ChatSession> getAllSessions() {
        return sessionRepo.findAllByOrderByUpdatedAtDesc();
    }

    @Override
    public void deleteSession(String sessionId) {
        sessionRepo.deleteById(sessionId);
    }
}