package com.poly.backend.controller;

import com.poly.backend.dao.OrderStatusDAO;
import com.poly.backend.dto.GuestOrderRequestDTO;
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

    @Autowired
    private OrderStatusDAO orderStatusDAO;

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

    /**
     * API 3: Khách vãng lai tiến hành đặt hàng (Guest Checkout)
     * URL: http://localhost:8080/api/orders/guest/place
     */
    @PostMapping("/guest/place")
    public ResponseEntity<?> placeGuestOrder(@RequestBody GuestOrderRequestDTO request) {
        try {
            // Service xử lý: Lưu Bill với customer_id = null -> Lưu OrderDetails từ mảng items
            OrderResponseDTO orderResponse = orderService.placeGuestOrder(request);
            return ResponseEntity.ok(orderResponse);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi hệ thống khi đặt hàng: " + e.getMessage());
        }
    }

    /**
     * API 4: Lấy chi tiết đơn hàng theo Order Code (Dùng cho cả user & vãng lai)
     * URL: http://localhost:8080/api/orders/code/{orderCode}
     */
    @GetMapping("/code/{orderCode}")
    public ResponseEntity<?> getOrderByCode(@PathVariable String orderCode) {
        try {
            OrderResponseDTO order = orderService.getOrderByCode(orderCode);
            return ResponseEntity.ok(order);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi lấy đơn hàng: " + e.getMessage());
        }
    }

    /**
     * API 5: [ADMIN] Lấy tất cả danh sách đơn hàng
     * URL: http://localhost:8080/api/orders/admin/all
     */
    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllOrdersAdmin() {
        try {
            return ResponseEntity.ok(orderService.getAllOrdersForAdmin());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi lấy danh sách đơn hàng: " + e.getMessage());
        }
    }

    /**
     * API 6: [ADMIN] Cập nhật trạng thái đơn hàng
     * URL: http://localhost:8080/api/orders/admin/{orderId}/status?statusId=...
     */
    @PutMapping("/admin/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestParam Integer statusId,
            @RequestParam(required = false) Integer employeeId,
            @RequestParam(required = false) Integer shipperId) {
        try {
            return ResponseEntity.ok(orderService.updateOrderStatus(orderId, statusId,employeeId, shipperId));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi cập nhật trạng thái: " + e.getMessage());
        }
    }

    /**
     * API 7: [SHIPPER] Lấy danh sách đơn hàng cần giao
     * URL: http://localhost:8080/api/orders/shipper/tasks
     */
    @GetMapping("/shipper/tasks")
    public ResponseEntity<?> getOrdersForShipper(@RequestParam Integer shipperId) { // Yêu cầu gửi kèm Shipper ID
        try {
            return ResponseEntity.ok(orderService.getOrdersForShipper(shipperId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi lấy nhiệm vụ giao hàng: " + e.getMessage());
        }
    }

    /**
     * API 8: Lấy danh sách tất cả các trạng thái đơn hàng từ DB
     */
    @GetMapping("/statuses")
    public ResponseEntity<?> getAllOrderStatuses() {
        try {
            return ResponseEntity.ok(orderStatusDAO.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi lấy trạng thái: " + e.getMessage());
        }
    }
}