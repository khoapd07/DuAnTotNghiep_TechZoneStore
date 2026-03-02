package com.poly.backend.service;

import com.poly.backend.dto.LoginRequest;
import com.poly.backend.dto.RegisterRequest;
import com.poly.backend.entity.Customer;
import com.poly.backend.entity.Role;
import com.poly.backend.entity.User;
import com.poly.backend.dao.RoleDAO;
import com.poly.backend.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Import thêm cái này
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    // Inject PasswordEncoder để mã hóa và giải mã
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Đăng ký (Mặc định tạo Customer)
    public User register(RegisterRequest req) {
        if (userDAO.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Tài khoản đã tồn tại. Vui lòng chọn tên khác!");
        }

        Customer customer = new Customer();
        customer.setUsername(req.getUsername());

        // Mã hóa mật khẩu
        customer.setPassword(passwordEncoder.encode(req.getPassword()));

        // Gán dữ liệu mặc định để tránh lỗi NOT NULL trong Database
        customer.setEmail(req.getUsername() + "@techzone.local"); // Tạo email ảo
        customer.setFullName(req.getUsername()); // Lấy username làm tên hiển thị ban đầu
        customer.setStatus(true);

        // Gán quyền User (Role ID = 0)
        Role userRole = roleDAO.findById(0)
                .orElseThrow(() -> new RuntimeException("Role User không tồn tại"));
        customer.setRole(userRole);

        return userDAO.save(customer);
    }

    // Đăng nhập
    public User login(LoginRequest req) {
        User user = userDAO.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // ĐÃ SỬA: Dùng hàm matches() của BCrypt để so sánh mật khẩu
        // Tham số 1: Mật khẩu người dùng nhập (chuỗi thường)
        // Tham số 2: Mật khẩu trong DB (chuỗi đã mã hóa)
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }
        return user;
    }
}