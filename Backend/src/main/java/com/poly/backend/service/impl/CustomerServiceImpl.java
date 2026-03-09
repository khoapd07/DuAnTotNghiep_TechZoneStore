package com.poly.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.CustomerDAO;
import com.poly.backend.dao.RoleDAO;
import com.poly.backend.entity.Customer;
import com.poly.backend.entity.Role;
import com.poly.backend.dto.CustomerDTO;
import com.poly.backend.service.CustomerService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;
    private final RoleDAO roleDAO; // Tiêm RoleDAO để set quyền
    private final PasswordEncoder passwordEncoder; // Tiêm PasswordEncoder để mã hóa pass

    @Override
    public List<CustomerDTO> findAll() {
        return customerDAO.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO findById(Integer id) {
        Customer customer = customerDAO.findById(id).orElse(null);
        return customer != null ? mapToDTO(customer) : null;
    }

    // THÊM MỚI: Hàm tạo khách hàng
    @Override
    public CustomerDTO create(CustomerDTO dto) {
        // --- BẮT ĐẦU VALIDATE ---
        if (customerDAO.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại!");
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty() && customerDAO.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email này đã được sử dụng!");
        }
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isEmpty() && customerDAO.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new IllegalArgumentException("Số điện thoại này đã được sử dụng!");
        }
        // --- KẾT THÚC VALIDATE ---
        Customer customer = new Customer();
        customer.setUsername(dto.getUsername());
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));
        customer.setFullName(dto.getFullName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setShippingAddress(dto.getShippingAddress());
        customer.setLoyaltyPoints(dto.getLoyaltyPoints() != null ? dto.getLoyaltyPoints() : 0);
        customer.setStatus(dto.getStatus() != null ? dto.getStatus() : true);

        // Mặc định gán quyền Customer (Role ID = 0, tùy theo DB của bạn)
        Role userRole = roleDAO.findById(0).orElseThrow(() -> new RuntimeException("Role không tồn tại"));
        customer.setRole(userRole);

        return mapToDTO(customerDAO.save(customer));
    }

    @Override
    public CustomerDTO save(CustomerDTO dto) {
        Customer customer = customerDAO.findById(dto.getUserId()).orElse(null);
        if(customer == null) throw new IllegalArgumentException("Không tìm thấy khách hàng!");

        // --- BẮT ĐẦU VALIDATE ---
        if (dto.getEmail() != null && !dto.getEmail().isEmpty() && customerDAO.existsByEmailAndUserIdNot(dto.getEmail(), dto.getUserId())) {
            throw new IllegalArgumentException("Email này đã được khách hàng khác sử dụng!");
        }
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isEmpty() && customerDAO.existsByPhoneNumberAndUserIdNot(dto.getPhoneNumber(), dto.getUserId())) {
            throw new IllegalArgumentException("Số điện thoại này đã được khách hàng khác sử dụng!");
        }
        // --- KẾT THÚC VALIDATE ---

        // Ghi đè dữ liệu mới (Nếu thiếu dòng nào ở đây, dữ liệu dòng đó sẽ không được cập nhật)
        customer.setFullName(dto.getFullName());
        customer.setEmail(dto.getEmail()); // <-- DÒNG BỊ THIẾU LÀ ĐÂY
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setShippingAddress(dto.getShippingAddress());

        if(dto.getLoyaltyPoints() != null) {
            customer.setLoyaltyPoints(dto.getLoyaltyPoints());
        }
        if(dto.getStatus() != null) {
            customer.setStatus(dto.getStatus());
        }

        return mapToDTO(customerDAO.save(customer));
    }

    @Override
    public void deleteById(Integer id) {
        customerDAO.deleteById(id);
    }

    @Override
    public String toggleStatus(Integer id) {
        Customer customer = customerDAO.findById(id).orElse(null);
        if (customer == null) return null;
        customer.setStatus(!customer.getStatus());
        customerDAO.save(customer);
        return customer.getStatus() ? "Đã mở khóa tài khoản" : "Đã khóa tài khoản";
    }

    private CustomerDTO mapToDTO(Customer customer) {
        return CustomerDTO.builder()
                .userId(customer.getUserId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .shippingAddress(customer.getShippingAddress())
                .loyaltyPoints(customer.getLoyaltyPoints())
                .status(customer.getStatus())
                .createdAt(customer.getCreatedAt()) // Map thêm ngày tạo (đảm bảo Entity User có trường này)
                .build();
    }
}