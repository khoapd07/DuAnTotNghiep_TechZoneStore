package com.poly.backend.controller;

import com.poly.backend.dto.CartItemRequestDTO;
import com.poly.backend.dto.CartResponseDTO;
import com.poly.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*") // Cho phép Vue.js (thường chạy port 5173 hoặc 8080) gọi sang không bị chặn
public class CartController {

    @Autowired
    private CartService cartService;

    // 1. Lấy thông tin giỏ hàng của User
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable Integer userId) {
        return ResponseEntity.ok(cartService.getCartDTOByUserId(userId));
    }

    // 2. Thêm sản phẩm / Cập nhật số lượng (Có kiểm tra tồn kho)
    @PostMapping("/{userId}/add")
    public ResponseEntity<?> addOrUpdateItem(
            @PathVariable Integer userId,
            @RequestBody CartItemRequestDTO request) {
        try {
            // Nếu thành công trả về cục DTO mới nhất
            return ResponseEntity.ok(cartService.addOrUpdateCartItem(userId, request));
        } catch (ResponseStatusException e) {
            // Nếu lỗi (ví dụ 400 - Hết hàng), trả đúng Status Code và chuỗi thông báo
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    // 3. Gộp giỏ hàng từ Local Storage (Dành cho lúc khách vừa Login)
    @PostMapping("/{userId}/merge")
    public ResponseEntity<?> mergeCart(
            @PathVariable Integer userId,
            @RequestBody List<CartItemRequestDTO> localCartItems) {
        try {
            return ResponseEntity.ok(cartService.mergeLocalCart(userId, localCartItems));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi gộp giỏ hàng: " + e.getMessage());
        }
    }

    // 4. Xóa 1 sản phẩm cụ thể khỏi giỏ
    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable Integer userId,
            @PathVariable Integer productId) {
        try {
            return ResponseEntity.ok(cartService.removeCartItem(userId, productId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
    }

    // 5. Xóa trắng giỏ hàng (Gọi sau khi đã thanh toán thành công)
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<?> clearCart(@PathVariable Integer userId) {
        try {
            return ResponseEntity.ok(cartService.clearCart(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi làm sạch giỏ hàng: " + e.getMessage());
        }
    }
}