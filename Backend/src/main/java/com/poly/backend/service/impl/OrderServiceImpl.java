package com.poly.backend.service.impl;

import com.poly.backend.dao.*;
import com.poly.backend.dto.*;
import com.poly.backend.entity.*;
import com.poly.backend.service.OrderService;
import com.poly.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDao;
    private final OrderDetailDAO orderDetailDao;
    private final ProductDAO productDAO;
    private final CustomerDAO customerDAO;
    private final CartService cartService;

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDto) {
        // 1. Lấy giỏ hàng (CartDTO)
        List<CartDTO> cartItems = cartService.findByCustomerId(orderDto.getCustomerId());
        if (cartItems.isEmpty()) throw new RuntimeException("Giỏ hàng đang trống!");

        // 2. Tính tổng tiền
        BigDecimal totalMoney = cartItems.stream()
                .map(item -> item.getTotalPrice() != null ? item.getTotalPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Khởi tạo đơn hàng
        Order order = new Order();

        // 4. Lấy Customer
        Customer customer = customerDAO.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"));

        order.setCustomer(customer);
        order.setNote(orderDto.getNote());
        order.setOrderDate(LocalDateTime.now());

        // Tự sinh mã đơn hàng để tránh lỗi Unique Key
        order.setOrderCode("ORD-" + System.currentTimeMillis());

        // --- ĐOẠN FIX LỖI QUAN TRỌNG NHẤT ---
        // Gán giá trị cho total_money để không bị lỗi NULL
        order.setTotalMoney(totalMoney);
        order.setFinalAmount(totalMoney);
        order.setDiscountAmount(BigDecimal.ZERO);
        // ------------------------------------

        // Lưu đơn hàng vào DB
        final Order savedOrder = orderDao.save(order);

        // 5. Lưu chi tiết hóa đơn (OrderDetail)
        for (CartDTO item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);

            Product product = productDAO.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + item.getProductId()));

            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getProductPrice());
            orderDetailDao.save(detail);
        }

        // 6. Xóa giỏ hàng sau khi đặt thành công
        cartService.clearCartByCustomerId(orderDto.getCustomerId());

        orderDto.setOrderId(savedOrder.getOrderId());
        orderDto.setTotalMoney(totalMoney);
        orderDto.setOrderCode(savedOrder.getOrderCode());

        return orderDto;
    }

    @Override
    public List<Order> findByCustomerId(Integer customerId) {
        return orderDao.findByCustomerId(customerId);
    }

    @Override
    public Order findById(Integer orderId) {
        return orderDao.findById(orderId).orElse(null);
    }

    @Override
    @Transactional
    public boolean deleteOrder(Integer orderId) {
        // 1. Kiểm tra xem đơn hàng có tồn tại không
        if (orderDao.existsById(orderId)) {
            // 2. Xóa đơn hàng
            // Lưu ý: Nhớ thêm cascade = CascadeType.ALL ở Entity Order để xóa luôn OrderDetail
            orderDao.deleteById(orderId);
            return true;
        }
        return false;
    }
}