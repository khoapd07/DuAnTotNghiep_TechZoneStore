package com.poly.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.CustomerDAO;
import com.poly.backend.entity.Customer;
import com.poly.backend.dto.CustomerDTO;
import com.poly.backend.service.CustomerService;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;

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

    @Override
    public CustomerDTO save(CustomerDTO dto) {
        Customer customer = customerDAO.findById(dto.getUserId()).orElse(null);
        if(customer == null) return null;

        customer.setFullName(dto.getFullName());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setShippingAddress(dto.getShippingAddress());

        // Cập nhật điểm
        if(dto.getLoyaltyPoints() != null) {
            customer.setLoyaltyPoints(dto.getLoyaltyPoints());
        }

        // Thêm dòng này để Cập nhật trạng thái từ Modal Form
        if(dto.getStatus() != null) {
            customer.setStatus(dto.getStatus());
        }

        Customer updatedCustomer = customerDAO.save(customer);
        return mapToDTO(updatedCustomer);
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

    // --- HÀM MAPPING ĐƯỢC CHUYỂN XUỐNG ĐÂY ---
    private CustomerDTO mapToDTO(Customer customer) {
        return CustomerDTO.builder()
                .userId(customer.getUserId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .shippingAddress(customer.getShippingAddress())
                .loyaltyPoints(customer.getLoyaltyPoints())
                .status(customer.getStatus())
                .build();
    }
}