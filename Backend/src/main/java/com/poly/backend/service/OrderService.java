package com.poly.backend.service;

import com.poly.backend.dto.OrderRequestDTO;
import com.poly.backend.dto.OrderResponseDTO;
import java.util.List;

public interface OrderService {
    // Chức năng Đặt hàng (Checkout)
    OrderResponseDTO placeOrder(Integer userId, OrderRequestDTO request);

    // Lấy lịch sử mua hàng
    List<OrderResponseDTO> getOrderHistory(Integer userId);
}