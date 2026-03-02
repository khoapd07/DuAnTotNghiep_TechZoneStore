package com.poly.backend.controller;

import com.poly.backend.dto.OrderRequestDTO;
import com.poly.backend.dto.OrderResponseDTO;
import com.poly.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*") // Tránh lỗi CORS khi Vue.js gọi API
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * API 1: Khách hàng tiến hành đặt hàng (Checkout)
     * Method: POST
     * URL: http://localhost:8080/api/orders/{userId}/place
     * Body JSON: { "note": "Giao giờ hành chính", "voucherCode": "OPENING" }
     */
    @PostMapping("/{userId}/place")
    public ResponseEntity<?> placeOrder(
            @PathVariable Integer userId,
            @RequestBody OrderRequestDTO request) {
        try {
            // Gọi Service xử lý luồng: Check kho -> Trừ kho -> Lưu Bill -> Xóa giỏ hàng
            OrderResponseDTO orderResponse = orderService.placeOrder(userId, request);
            return ResponseEntity.ok(orderResponse);

        } catch (ResponseStatusException e) {
            // Bắt các lỗi nghiệp vụ từ Service (như Hết hàng, Sai Voucher) và trả đúng HTTP Status
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());

        } catch (Exception e) {
            // Lỗi hệ thống bất ngờ
            return ResponseEntity.badRequest().body("Lỗi hệ thống khi đặt hàng: " + e.getMessage());
        }
    }

    /**
     * API 2: Lấy danh sách lịch sử mua hàng của khách
     * Method: GET
     * URL: http://localhost:8080/api/orders/{userId}/history
     */
    @GetMapping("/{userId}/history")
    public ResponseEntity<?> getOrderHistory(@PathVariable Integer userId) {
        try {
            List<OrderResponseDTO> history = orderService.getOrderHistory(userId);
            return ResponseEntity.ok(history);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi lấy lịch sử đơn hàng: " + e.getMessage());
        }
    }
}