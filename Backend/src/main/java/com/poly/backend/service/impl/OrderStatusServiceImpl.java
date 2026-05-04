package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.repository.OrderStatusRepository;
import com.poly.backend.entity.OrderStatus;
import com.poly.backend.service.OrderStatusService;

@Service
@RequiredArgsConstructor
public class OrderStatusServiceImpl implements OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;

    public List<OrderStatus> findAll() {
        return orderStatusRepository.findAll();
    }

    public OrderStatus findById(Integer id) {
        return orderStatusRepository.findById(id).orElse(null);
    }

    public OrderStatus save(OrderStatus status) {
        return orderStatusRepository.save(status);
    }

    public void deleteById(Integer id) {
        orderStatusRepository.deleteById(id);
    }
}