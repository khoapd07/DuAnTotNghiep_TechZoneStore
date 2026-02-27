package com.poly.backend.service;

import com.poly.backend.dto.OrderDTO;
import com.poly.backend.entity.Order;
import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDto);
    List<Order> findByCustomerId(Integer customerId);
    Order findById(Integer orderId);
    boolean deleteOrder(Integer orderId);
}