package com.poly.backend.AIChat.service;

import com.poly.backend.AIChat.entity.ChatMessage;
import java.util.List;

public interface ChatMessageService {
    // Lưu 1 dòng tin nhắn (của Khách, AI, hoặc Nhân viên)
    ChatMessage saveMessage(String sessionId, String senderType, String content);

    // Lấy lịch sử chat để hiển thị cho Staff xem khi giành quyền
    List<ChatMessage> getChatHistory(String sessionId);
}