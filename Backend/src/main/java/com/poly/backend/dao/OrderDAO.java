package com.poly.backend.dao;

import com.poly.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDAO extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.customer.userId = :customerId")
    List<Order> findByCustomerId(@Param("customerId") Integer customerId);
}