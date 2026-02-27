package com.poly.backend.dao;

import com.poly.backend.entity.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDAO extends JpaRepository<Cart, Integer> {

    // Tìm giỏ hàng theo ID của Customer (trường khóa chính là userId)
    @Query("SELECT c FROM Cart c WHERE c.customer.userId = :customerId")
    List<Cart> findByCustomerId(@Param("customerId") Integer customerId);

    // Xóa giỏ hàng theo ID của Customer
    @Transactional
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.customer.userId = :customerId")
    void deleteByCustomerId(@Param("customerId") Integer customerId);
}