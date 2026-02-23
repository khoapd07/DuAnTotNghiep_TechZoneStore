package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.OrderDAO;
import com.poly.backend.entity.Order;
import com.poly.backend.service.OrderService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO;

    public List<Order> findAll() {
        return orderDAO.findAll();
    }

    public Order findById(Integer id) {
        return orderDAO.findById(id).orElse(null);
    }

    public Order save(Order order) {
        return orderDAO.save(order);
    }

    public void deleteById(Integer id) {
        orderDAO.deleteById(id);
    }
}