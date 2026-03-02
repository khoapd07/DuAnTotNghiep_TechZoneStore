package com.poly.backend.dao;
import com.poly.backend.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailDAO extends JpaRepository<OrderDetail, Integer> {}