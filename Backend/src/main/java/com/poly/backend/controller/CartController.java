package com.poly.backend.controller;

import com.poly.backend.dto.CartDTO;
import com.poly.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {

    @Autowired
    private CartService cartService;

    // Xem giỏ hàng của khách
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCart(@PathVariable Integer customerId) {
        return ResponseEntity.ok(cartService.findByCustomerId(customerId));
    }

    // Thêm sản phẩm vào giỏ
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartDTO cartDto) {
        try {
            cartService.addItem(cartDto);
            return ResponseEntity.ok("Đã thêm sản phẩm vào giỏ hàng!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi thêm giỏ hàng: " + e.getMessage());
        }
    }

    // Cập nhật số lượng sản phẩm
    @PutMapping("/update")
    public ResponseEntity<?> updateQuantity(@RequestBody CartDTO cartDto) {
        cartService.updateQuantity(cartDto);
        return ResponseEntity.ok("Cập nhật số lượng thành công!");
    }

    // Xóa một sản phẩm khỏi giỏ
    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Integer cartId) {
        cartService.deleteById(cartId);
        return ResponseEntity.ok("Đã xóa sản phẩm khỏi giỏ hàng!");
    }

    // Xóa sạch giỏ hàng (Gọi sau khi checkout thành công hoặc khách tự dọn)
    @DeleteMapping("/clear/{customerId}")
    public ResponseEntity<?> clearCart(@PathVariable Integer customerId) {
        cartService.clearCartByCustomerId(customerId);
        return ResponseEntity.ok("Đã dọn dẹp giỏ hàng!");
    }
}