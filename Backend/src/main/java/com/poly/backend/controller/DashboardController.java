package com.poly.backend.controller;

import com.poly.backend.dto.DashboardStatsDTO;
import com.poly.backend.dto.OrderResponseDTO;
import com.poly.backend.service.DashboardService;
import com.poly.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@CrossOrigin("*") // Cho phép Vue.js gọi API từ port khác (thường là 5173 hoặc 8080)
public class DashboardController {

    private final DashboardService dashboardService;
    private final OrderService orderService;

    // 1. API lấy các con số tổng quát (Cards)
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }

    // 2. API lấy danh sách đơn hàng gần đây cho bảng dữ liệu
    @GetMapping("/recent-orders")
    public ResponseEntity<List<OrderResponseDTO>> getRecentOrders() {
        // Lấy 5 đơn hàng mới nhất
        return ResponseEntity.ok(orderService.getRecentOrders(5));
    }
}