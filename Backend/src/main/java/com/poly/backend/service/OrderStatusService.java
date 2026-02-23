package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.OrderStatus;

public interface OrderStatusService {
    List<OrderStatus> findAll();
    OrderStatus findById(Integer id);
    OrderStatus save(OrderStatus status);
    void deleteById(Integer id);
}