package com.poly.backend.service;

import com.poly.backend.dto.LoginRequest;
import com.poly.backend.dto.RegisterRequest;
import com.poly.backend.entity.Customer;
import com.poly.backend.entity.Role;
import com.poly.backend.entity.User;
import com.poly.backend.dao.RoleDAO;
import com.poly.backend.dao.UserDAO;
import com.poly.backend.dao.CustomerDAO; // Thêm dòng này
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CustomerDAO customerDAO; // Dùng CustomerDAO cho an toàn

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Đăng ký (Mặc định tạo Customer)
    public User register(RegisterRequest req) {
        if (userDAO.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Tài khoản đã tồn tại. Vui lòng chọn tên khác!");
        }

        Customer customer = new Customer();
        customer.setUsername(req.getUsername());
        customer.setPassword(passwordEncoder.encode(req.getPassword()));

        // Mẹo: Tạm lấy username làm tên hiển thị luôn để Frontend đỡ bị trống,
        // khách hàng có thể đổi lại tên thật sau.
//        customer.setFullName(req.getUsername());

        // Đảm bảo các thuộc tính của Customer không bị rỗng
        customer.setLoyaltyPoints(0);
        customer.setStatus(true);

        // Gán quyền User (Role ID = 0)
        Role userRole = roleDAO.findById(0)
                .orElseThrow(() -> new RuntimeException("Role User không tồn tại"));
        customer.setRole(userRole);

        // DÙNG customerDAO THAY VÌ userDAO
        return customerDAO.save(customer);
    }

    // Đăng nhập
    public User login(LoginRequest req) {
        User user = userDAO.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu!");
        }

        // Cấm login nếu tài khoản bị khóa
        if (!user.getStatus()) {
            throw new RuntimeException("Tài khoản của bạn đã bị khóa!");
        }

        return user;
    }
}