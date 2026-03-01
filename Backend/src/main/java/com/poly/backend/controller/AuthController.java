package com.poly.backend.controller;

import com.poly.backend.dto.AuthResponse;
import com.poly.backend.dto.LoginRequest;
import com.poly.backend.dto.RegisterRequest;
import com.poly.backend.entity.User;
import com.poly.backend.security.JwtTokenProvider;
import com.poly.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            User createdUser = authService.register(req);
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            // 1. Xác thực User
            User user = authService.login(req);

            // 2. Tạo JWT Token
            String token = jwtTokenProvider.generateToken(user);

            // 3. Đóng gói kết quả trả về
            AuthResponse response = new AuthResponse(
                    token,
                    user.getUserId(),
                    user.getUsername(),
                    user.getRole().getRoleName()
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}