package com.poly.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poly.backend.entity.Order;

@Repository
public interface OrderDAO extends JpaRepository<Order, Integer> {
}