package com.poly.backend.service;

import com.poly.backend.dto.LoginRequest;
import com.poly.backend.dto.RegisterRequest;
import com.poly.backend.entity.Customer;
import com.poly.backend.entity.Role;
import com.poly.backend.entity.User;
import com.poly.backend.dao.RoleDAO;
import com.poly.backend.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    // Đăng ký (Mặc định tạo Customer)
    public User register(RegisterRequest req) {
        if (userDAO.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username đã tồn tại!");
        }
        if (userDAO.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        // Tạo Customer (sẽ tự động lưu vào cả bảng Users và Customers)
        Customer customer = new Customer();
        customer.setUsername(req.getUsername());
        customer.setPassword(req.getPassword()); // TODO: Sau này nhớ mã hóa BCrypt ở đây
        customer.setEmail(req.getEmail());
        customer.setFullName(req.getFullName());
        customer.setPhoneNumber(req.getPhoneNumber());
        customer.setGender(req.getGender());
        customer.setAddress(req.getAddress());
        customer.setShippingAddress(req.getAddress()); // Mặc định địa chỉ giao hàng = địa chỉ nhà
        customer.setStatus(true);

        // Gán quyền User (Role ID = 0 theo file SQL)
        Role userRole = roleDAO.findById(0)
                .orElseThrow(() -> new RuntimeException("Role User không tồn tại"));
        customer.setRole(userRole);

        return userDAO.save(customer);
    }

    // Đăng nhập
    public User login(LoginRequest req) {
        User user = userDAO.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // So sánh pass (Đang để text thường để test, sau này dùng encoder.matches())
        if (!user.getPassword().equals(req.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }
        return user;
    }
}