package com.poly.backend.service;

import com.poly.backend.dto.AuthResponse;
import com.poly.backend.dto.LoginRequest;
import com.poly.backend.dto.RegisterRequest;
import com.poly.backend.entity.Customer;
import com.poly.backend.entity.Role;
import com.poly.backend.entity.User;
import com.poly.backend.dao.RoleRepository;
import com.poly.backend.dao.UserRepository;
import com.poly.backend.dao.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Collections;
import java.util.UUID;

import com.poly.backend.security.JwtTokenProvider;

@Service
public class AuthService {

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // Đăng ký (Mặc định tạo Customer)
    public User register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
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
        Role userRole = roleRepository.findById(0)
                .orElseThrow(() -> new RuntimeException("Role User không tồn tại"));
        customer.setRole(userRole);

        // DÙNG customerDAO THAY VÌ userDAO
        return customerRepository.save(customer);
    }

    // Đăng nhập
    public User login(LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
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

    public AuthResponse loginWithGoogle(String credential) throws Exception {
        NetHttpTransport transport = new NetHttpTransport();
        GsonFactory jsonFactory = new GsonFactory();

        // Cấu hình verifier (trình xác thực) với Client ID của bạn
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        // Verify (xác thực) token do Frontend gửi lên
        GoogleIdToken idToken = verifier.verify(credential);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Lấy thông tin từ Google Payload (dữ liệu tải)
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            // Kiểm tra xem User (người dùng) đã tồn tại trong hệ thống chưa (dùng email làm username)
            User user = userRepository.findByUsername(email).orElse(null);

            if (user == null) {
                // Nếu chưa có, tiến hành Register (đăng ký) tự động
                Customer customer = new Customer();
                customer.setUsername(email); // Dùng email làm tên đăng nhập
                customer.setPassword(passwordEncoder.encode("GOOGLE_LOGIN_NO_PASSWORD")); // Đặt mật khẩu ảo


                customer.setFullName(name);  // Lấy họ tên thật từ Google
                customer.setEmail(email);    // Lưu luôn email vào cột email

                customer.setLoyaltyPoints(0);
                customer.setStatus(true);

                Role userRole = roleRepository.findById(0)
                        .orElseThrow(() -> new RuntimeException("Role User không tồn tại"));
                customer.setRole(userRole);

                user = customerRepository.save(customer); // Lưu vào Database (cơ sở dữ liệu)
            }

            // Kiểm tra trạng thái khóa tài khoản
            if (!user.getStatus()) {
                throw new RuntimeException("Tài khoản của bạn đã bị khóa!");
            }

            // Generate (tạo) JWT Token (mã thông báo) chứa tên bên trong
            String token = jwtTokenProvider.generateToken(user);

            // Return (trả về) AuthResponse (kết quả phản hồi)
            return new AuthResponse(
                    token,
                    user.getUserId(),
                    name,
                    user.getRole().getRoleName()
            );
        } else {
            throw new RuntimeException("Token Google không hợp lệ!");
        }
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email này!"));

        // Tạo token ngẫu nhiên
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        userRepository.save(user);

        // Tạo link reset password
        String resetLink = frontendUrl + "/reset-password?token=" + token;

        // Gửi email
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Token không hợp lệ hoặc đã hết hạn!"));

        // Cập nhật mật khẩu mới và xóa token
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

}