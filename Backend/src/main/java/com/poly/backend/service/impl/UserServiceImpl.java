package com.poly.backend.service.impl;

import java.util.List;

import com.poly.backend.dto.ChangePasswordDTO;
import com.poly.backend.dto.UserProfileDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.UserDAO;
import com.poly.backend.entity.User;
import com.poly.backend.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public User findById(Integer id) {
        return userDAO.findById(id).orElse(null);
    }

    public User save(User user) {
        return userDAO.save(user);
    }

    public void deleteById(Integer id) {
        userDAO.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username)
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
            if (userDAO.existsByEmailAndUserIdNot(dto.getEmail(), user.getUserId())) {
                throw new RuntimeException("Email này đã được sử dụng bởi tài khoản khác! Vui lòng dùng email khác.");
            }
            user.setEmail(dto.getEmail());
        }
        user.setAddress(dto.getAddress());
        // Các trường khác giữ nguyên
        return userDAO.save(user);
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
        userDAO.save(user);
    }

}