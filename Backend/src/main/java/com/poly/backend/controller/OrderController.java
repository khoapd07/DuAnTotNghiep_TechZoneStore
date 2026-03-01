package com.poly.backend.controller;

import com.poly.backend.dto.OrderDTO;
import com.poly.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 1. Đặt hàng
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody OrderDTO orderDto) {
        try {
            OrderDTO createdOrder = orderService.createOrder(orderDto);
            return ResponseEntity.status(201).body(createdOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi đặt hàng: " + e.getMessage());
        }
    }

    // 2. Xem lịch sử
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerOrders(@PathVariable Integer customerId) {
        return ResponseEntity.ok(orderService.findByCustomerId(customerId));
    }

    // 3. Xóa đơn hàng (Hủy vĩnh viễn)
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer id) {
        try {
            boolean isDeleted = orderService.deleteOrder(id);
            if (isDeleted) {
                return ResponseEntity.ok("Đã xóa vĩnh viễn đơn hàng ID: " + id);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa đơn: " + e.getMessage());
        }
    }
}