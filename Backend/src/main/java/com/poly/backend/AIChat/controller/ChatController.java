package com.poly.backend.AIChat.controller;

import com.poly.backend.AIChat.entity.ChatMessage;
import com.poly.backend.AIChat.entity.ChatSession;
import com.poly.backend.AIChat.service.ChatMessageService;
import com.poly.backend.AIChat.service.ChatSessionService;
import com.poly.backend.AIChat.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
//@CrossOrigin("*") // Cho phép Vue.js gọi API mà không bị lỗi CORS
public class ChatController {

    @Autowired
    private ChatSessionService sessionService;

    @Autowired
    private ChatMessageService messageService;

    // TIÊM GEMINI SERVICE VÀO ĐÂY ĐỂ GỌI AI
    @Autowired
    private GeminiService geminiService;

    // 1. API khởi tạo hoặc lấy thông tin Session (Vue.js gọi khi khách mở khung chat)
    @PostMapping("/session")
    public ResponseEntity<ChatSession> getOrCreateSession(@RequestBody Map<String, Object> payload) {
        String sessionId = (String) payload.get("sessionId");
        Integer customerId = payload.containsKey("customerId") ? (Integer) payload.get("customerId") : null;

        ChatSession session = sessionService.createOrGetSession(sessionId, customerId);
        return ResponseEntity.ok(session);
    }

    // 2. API kiểm tra trạng thái Session hiện tại
    @GetMapping("/session/{sessionId}/status")
    public ResponseEntity<ChatSession> getSessionStatus(@PathVariable String sessionId) {
        ChatSession session = sessionService.getSession(sessionId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(session);
    }

    // =========================================================================
    // 3. API LÕI: LƯU TIN NHẮN VÀ TÍCH HỢP TRỰC TIẾP AI (THAY THẾ N8N)
    // =========================================================================
    @PostMapping("/message")
    public ResponseEntity<ChatMessage> saveMessage(@RequestBody Map<String, String> payload) {
        String sessionId = payload.get("sessionId");
        String senderType = payload.get("senderType");
        String content = payload.get("content");

        // DÒNG FIX LỖI: Đảm bảo session chắn chắn phải tồn tại trong DB trước khi lưu tin nhắn
        sessionService.createOrGetSession(sessionId, null);

        // Bước 1: Lưu ngay tin nhắn của người gửi vào Database
        ChatMessage savedMessage = messageService.saveMessage(sessionId, senderType, content);

        // Bước 2: Nếu người nhắn là KHÁCH HÀNG... (giữ nguyên code cũ bên dưới)
        if ("CUSTOMER".equals(senderType)) {
            ChatSession session = sessionService.getSession(sessionId);
            // Nếu trạng thái đang là AI_HANDLING -> Gọi Gemini trả lời
            if (session != null && "AI_HANDLING".equals(session.getStatus())) {

                // AI Bắt đầu suy nghĩ và trả lời
                String aiReply = geminiService.askGemini(content);

                // Lưu câu trả lời của AI vào Database
                ChatMessage aiMessage = messageService.saveMessage(sessionId, "AI", aiReply);

                // Trả về câu trả lời của AI cho Vue.js hiển thị lên màn hình
                return ResponseEntity.ok(aiMessage);
            }
        }

        // Nếu Staff đang chat (trạng thái STAFF_HANDLING) hoặc chính Staff là người gửi tin
        // Thì hệ thống chỉ trả về tin nhắn gốc (không gọi AI)
        return ResponseEntity.ok(savedMessage);
    }

    // 4. API Thay đổi trạng thái Chat (Giành quyền hoặc Trả cho AI)
    @PutMapping("/session/{sessionId}/status")
    public ResponseEntity<ChatSession> changeSessionStatus(@PathVariable String sessionId, @RequestBody Map<String, String> payload) {
        String status = payload.get("status"); // Sẽ nhận "STAFF_HANDLING" hoặc "AI_HANDLING"

        // Gọi hàm updateStatus đã có sẵn trong Service
        ChatSession updatedSession = sessionService.updateStatus(sessionId, status, null);
        return ResponseEntity.ok(updatedSession);
    }

    // 5. API lấy toàn bộ lịch sử chat để nhân viên đọc trước khi trả lời khách
    @GetMapping("/session/{sessionId}/messages")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String sessionId) {
        List<ChatMessage> history = messageService.getChatHistory(sessionId);
        return ResponseEntity.ok(history);
    }

    // 6. API cho Admin lấy danh sách toàn bộ phiên chat
    @GetMapping("/sessions")
    public ResponseEntity<List<ChatSession>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    // 7. API Xóa toàn bộ lịch sử của 1 phiên chat
    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable String sessionId) {
        sessionService.deleteSession(sessionId);
        return ResponseEntity.ok().build();
    }
}