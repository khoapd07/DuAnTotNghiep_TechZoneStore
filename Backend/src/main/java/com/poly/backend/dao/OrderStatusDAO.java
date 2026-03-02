package com.poly.backend.dao;
import com.poly.backend.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusDAO extends JpaRepository<OrderStatus, Integer> {}