package com.poly.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poly.backend.entity.OrderDetail;

@Repository
public interface OrderDetailDAO extends JpaRepository<OrderDetail, Integer> {
}