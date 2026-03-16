package com.poly.backend.AIChat.service.impl;

import com.poly.backend.AIChat.dao.ChatMessageRepository;
import com.poly.backend.AIChat.entity.ChatMessage;
import com.poly.backend.AIChat.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageRepository messageRepo;

    @Override
    public ChatMessage saveMessage(String sessionId, String senderType, String content) {
        ChatMessage message = new ChatMessage();
        message.setSessionId(sessionId);
        message.setSenderType(senderType); // Phải là "CUSTOMER", "AI", hoặc "STAFF"
        message.setContent(content);
        return messageRepo.save(message);
    }

    @Override
    public List<ChatMessage> getChatHistory(String sessionId) {
        return messageRepo.findBySessionIdOrderByCreatedAtAsc(sessionId);
    }
}