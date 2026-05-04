package com.poly.backend.service.impl;

import java.util.List;

import com.poly.backend.dto.ChangePasswordDTO;
import com.poly.backend.dto.UserProfileDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.repository.UserRepository;
import com.poly.backend.entity.User;
import com.poly.backend.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
    }

    @Override
    public User updateProfile(String username, UserProfileDTO dto) {
        User user = findByUsername(username);
        user.setFullName(dto.getFullName());
        user.setPhoneNumber(dto.getPhoneNumber());
//        user.setEmail(dto.getEmail());
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            // Kiểm tra xem email này có bị User KHÁC (khác userId hiện tại) chiếm dụng chưa
            if (userRepository.existsByEmailAndUserIdNot(dto.getEmail(), user.getUserId())) {
                throw new RuntimeException("Email này đã được sử dụng bởi tài khoản khác! Vui lòng dùng email khác.");
            }
            user.setEmail(dto.getEmail());
        }
        user.setAddress(dto.getAddress());
        // Các trường khác giữ nguyên
        return userRepository.save(user);
    }

    @Override
    public void changePassword(String username, ChangePasswordDTO dto) {
        User user = findByUsername(username);

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không chính xác!");
        }

        // Kiểm tra xác nhận mật khẩu
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp!");
        }

        // Mã hóa mật khẩu mới và lưu
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

}