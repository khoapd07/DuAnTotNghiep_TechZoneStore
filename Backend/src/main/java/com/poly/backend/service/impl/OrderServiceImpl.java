package com.poly.backend.service.impl;

import com.poly.backend.dao.*;
import com.poly.backend.dto.OrderDetailResponseDTO;
import com.poly.backend.dto.OrderRequestDTO;
import com.poly.backend.dto.OrderResponseDTO;
import com.poly.backend.entity.*;
import com.poly.backend.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private OrderDAO orderDAO;
    @Autowired private OrderStatusDAO orderStatusDAO;
    @Autowired private CartDAO cartDAO;
    @Autowired private ProductDAO productDAO;
    @Autowired private VoucherDAO voucherDAO;
    @Autowired private UserDAO userDAO;

    @Override
    @Transactional // Rất quan trọng: Bị lỗi ở bất kỳ bước nào là Rollback toàn bộ, không trừ tiền oan
    public OrderResponseDTO placeOrder(Integer userId, OrderRequestDTO request) {

        // 1. Lấy Giỏ hàng & Kiểm tra rỗng
        Cart cart = cartDAO.findByUser_UserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy giỏ hàng"));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giỏ hàng đang trống!");
        }

        User customer = userDAO.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy User"));

        // 2. Tạo đơn hàng mới
        Order order = new Order();
        order.setCustomer(customer);
        order.setNote(request.getNote());

        // Random mã đơn hàng (VD: TZ-20260302-XXXX)
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        order.setOrderCode("TZ-" + timeStamp + "-" + (int)(Math.random() * 10000));

        // Mặc định trạng thái là 0 (Pending)
        OrderStatus status = orderStatusDAO.findById(0)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi trạng thái đơn hàng"));
        order.setStatus(status);

        // 3. Xử lý từng món hàng, Tính tiền & Trừ tồn kho
        BigDecimal totalMoney = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (CartItem item : cart.getCartItems()) {
            Product product = item.getProduct();

            // Kéo data mới nhất từ DB để kiểm tra tồn kho lần cuối trước khi chốt đơn
            Product currentProduct = productDAO.findById(product.getProductId()).get();
            if (currentProduct.getStockQuantity() < item.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Sản phẩm '" + currentProduct.getName() + "' chỉ còn " + currentProduct.getStockQuantity() + " chiếc!");
            }

            // Trừ tồn kho
            currentProduct.setStockQuantity(currentProduct.getStockQuantity() - item.getQuantity());
            productDAO.save(currentProduct);

            // Chốt giá bán (Có sale lấy sale, không có lấy giá gốc)
            BigDecimal applyPrice = (currentProduct.getSalePrice() != null && currentProduct.getSalePrice().compareTo(BigDecimal.ZERO) > 0)
                    ? currentProduct.getSalePrice() : currentProduct.getPrice();

            totalMoney = totalMoney.add(applyPrice.multiply(BigDecimal.valueOf(item.getQuantity())));

            // Thêm vào chi tiết hóa đơn
            orderDetails.add(OrderDetail.builder()
                    .order(order)
                    .product(currentProduct)
                    .quantity(item.getQuantity())
                    .price(applyPrice) // LƯU GIÁ THỰC TẾ LÚC MUA
                    .build());
        }

        order.setTotalMoney(totalMoney);
        order.setOrderDetails(orderDetails);

        // 4. Áp dụng Voucher (Nếu khách có nhập)
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (request.getVoucherCode() != null && !request.getVoucherCode().trim().isEmpty()) {
            Voucher voucher = voucherDAO.findByCode(request.getVoucherCode())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá không hợp lệ"));

            // Validate sơ bộ voucher
            if (!voucher.getStatus() || voucher.getQuantity() <= 0 || voucher.getEndDate().isBefore(LocalDateTime.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá đã hết hạn hoặc hết lượt dùng");
            }
            if (totalMoney.compareTo(voucher.getMinOrderValue()) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đơn hàng chưa đạt giá trị tối thiểu để dùng mã này");
            }

            discountAmount = voucher.getDiscountAmount();
            order.setVoucher(voucher);

            // Trừ lượt dùng voucher
            voucher.setQuantity(voucher.getQuantity() - 1);
            voucherDAO.save(voucher);
        }

        order.setDiscountAmount(discountAmount);

        // Tính tiền thực trả (Không để âm tiền)
        BigDecimal finalAmount = totalMoney.subtract(discountAmount);
        order.setFinalAmount(finalAmount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : finalAmount);

        // 5. Lưu hóa đơn vào Database
        Order savedOrder = orderDAO.save(order);

        // 6. Dọn sạch Giỏ hàng
        cart.getCartItems().clear();
        cartDAO.save(cart);

        return mapToDTO(savedOrder);
    }

    @Override
    public List<OrderResponseDTO> getOrderHistory(Integer userId) {
        return orderDAO.findByCustomer_UserIdOrderByOrderDateDesc(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Hàm Helper chuyển từ Entity -> DTO
    private OrderResponseDTO mapToDTO(Order order) {
        List<OrderDetailResponseDTO> detailDTOs = order.getOrderDetails().stream().map(detail ->
                OrderDetailResponseDTO.builder()
                        .productId(detail.getProduct().getProductId())
                        .productName(detail.getProduct().getName())
                        .imageUrl(detail.getProduct().getImageUrl())
                        .quantity(detail.getQuantity())
                        .price(detail.getPrice())
                        .subTotal(detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                        .build()
        ).collect(Collectors.toList());

        return OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .orderCode(order.getOrderCode())
                .orderDate(order.getOrderDate())
                .totalMoney(order.getTotalMoney())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .note(order.getNote())
                .statusName(order.getStatus() != null ? order.getStatus().getStatusName() : "Unknown")
                .orderDetails(detailDTOs)
                .build();
    }
}