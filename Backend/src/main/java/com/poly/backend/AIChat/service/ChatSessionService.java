package com.poly.backend.AIChat.service;

import com.poly.backend.AIChat.entity.ChatSession;

import java.util.List;

public interface ChatSessionService {
    // Tạo session mới nếu chưa có, hoặc trả về session cũ nếu khách đang chat dở
    ChatSession createOrGetSession(String sessionId, Integer customerId);

    // Cập nhật trạng thái (VD: Staff giành quyền thì chuyển thành "STAFF_HANDLING")
    ChatSession updateStatus(String sessionId, String status, Integer staffId);

    // Lấy thông tin 1 phiên chat để kiểm tra quyền
    ChatSession getSession(String sessionId);

    List<ChatSession> getAllSessions();

    void deleteSession(String sessionId);
}