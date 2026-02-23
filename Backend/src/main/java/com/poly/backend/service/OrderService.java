package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.Order;

public interface OrderService {
    List<Order> findAll();
    Order findById(Integer id);
    Order save(Order order);
    void deleteById(Integer id);
}