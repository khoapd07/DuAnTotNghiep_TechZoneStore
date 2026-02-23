package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.OrderStatusDAO;
import com.poly.backend.entity.OrderStatus;
import com.poly.backend.service.OrderStatusService;

@Service
@RequiredArgsConstructor
public class OrderStatusServiceImpl implements OrderStatusService {

    private final OrderStatusDAO orderStatusDAO;

    public List<OrderStatus> findAll() {
        return orderStatusDAO.findAll();
    }

    public OrderStatus findById(Integer id) {
        return orderStatusDAO.findById(id).orElse(null);
    }

    public OrderStatus save(OrderStatus status) {
        return orderStatusDAO.save(status);
    }

    public void deleteById(Integer id) {
        orderStatusDAO.deleteById(id);
    }
}