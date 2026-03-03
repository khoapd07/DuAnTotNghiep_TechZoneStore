package com.poly.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.poly.backend.service.UserService;
import com.poly.backend.entity.User;
import com.poly.backend.dto.UserProfileDTO;
import com.poly.backend.dto.ChangePasswordDTO;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    // Lấy thông tin profile dựa vào userId
    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Integer userId) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy người dùng");
        }

        UserProfileDTO dto = new UserProfileDTO();
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());

        return ResponseEntity.ok(dto);
    }

    // Cập nhật profile dựa vào userId
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(@PathVariable Integer userId, @RequestBody UserProfileDTO dto) {
        try {
            User user = userService.findById(userId);
            if (user == null) return ResponseEntity.badRequest().body("Không tìm thấy người dùng");

            user.setFullName(dto.getFullName());
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setEmail(dto.getEmail());
            user.setAddress(dto.getAddress());
            userService.save(user);

            return ResponseEntity.ok("Cập nhật thông tin hồ sơ thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Đổi mật khẩu dựa vào username (hoặc userId tùy logic của bạn, ở đây tôi gọi hàm có sẵn trong Service)
    @PutMapping("/{userId}/password")
    public ResponseEntity<?> changePassword(@PathVariable Integer userId, @RequestBody ChangePasswordDTO dto) {
        try {
            User user = userService.findById(userId);
            if (user == null) return ResponseEntity.badRequest().body("Không tìm thấy người dùng");

            userService.changePassword(user.getUsername(), dto);
            return ResponseEntity.ok("Đổi mật khẩu thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}