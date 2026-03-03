package com.poly.backend.dao;

import com.poly.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDAO extends JpaRepository<Order, Integer> {
    // Tìm lịch sử đơn hàng của 1 User, xếp mới nhất lên đầu
    List<Order> findByCustomer_UserIdOrderByOrderDateDesc(Integer userId);

    Optional<Order> findByOrderCode(String orderCode);
}