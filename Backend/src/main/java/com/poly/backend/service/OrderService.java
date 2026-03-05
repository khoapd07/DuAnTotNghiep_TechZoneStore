package com.poly.backend.service;

import com.poly.backend.dto.GuestOrderRequestDTO;
import com.poly.backend.dto.OrderRequestDTO;
import com.poly.backend.dto.OrderResponseDTO;
import java.util.List;

public interface OrderService {
    // Chức năng Đặt hàng (Checkout)
    OrderResponseDTO placeOrder(Integer userId, OrderRequestDTO request);

    // Chức năng Đặt hàng (Checkout) cho Khách vãng lai (Guest)
    OrderResponseDTO placeGuestOrder(GuestOrderRequestDTO request);

    // Lấy lịch sử mua hàng
    List<OrderResponseDTO> getOrderHistory(Integer userId);

    OrderResponseDTO getOrderByCode(String orderCode);
    // Thêm khai báo này vào Interface OrderService
    List<OrderResponseDTO> getRecentOrders(int limit);


}