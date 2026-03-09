package com.poly.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poly.backend.entity.Customer;

@Repository
public interface CustomerDAO extends JpaRepository<Customer, Integer> {

    // Kiểm tra lúc tạo mới
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    // Kiểm tra lúc cập nhật (Loại trừ ID của chính khách hàng đó ra)
    boolean existsByEmailAndUserIdNot(String email, Integer userId);
    boolean existsByPhoneNumberAndUserIdNot(String phoneNumber, Integer userId);
}