package com.poly.backend.controller;

import com.poly.backend.dto.AuthResponse;
import com.poly.backend.dto.GoogleLoginRequest;
import com.poly.backend.dto.LoginRequest;
import com.poly.backend.dto.RegisterRequest;
import com.poly.backend.dto.ForgotPasswordRequest;
import com.poly.backend.dto.ResetPasswordRequest;
import com.poly.backend.entity.User;
import com.poly.backend.security.JwtTokenProvider;
import com.poly.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@CrossOrigin("*")
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

    @PostMapping("/google")
    public ResponseEntity<?> authenticateGoogle(@RequestBody GoogleLoginRequest request) {
        try {
            AuthResponse authResponse = authService.loginWithGoogle(request.getToken());
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Đăng nhập Google thất bại: " + e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            authService.forgotPassword(request.getEmail());
            return ResponseEntity.ok(Collections.singletonMap("message", "Link đặt lại mật khẩu đã được gửi đến email của bạn."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(Collections.singletonMap("message", "Đặt lại mật khẩu thành công."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}