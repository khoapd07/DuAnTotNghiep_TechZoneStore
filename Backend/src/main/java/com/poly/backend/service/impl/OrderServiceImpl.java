package com.poly.backend.service.impl;

import com.poly.backend.dao.*;
import com.poly.backend.dto.*;
import com.poly.backend.entity.*;
import com.poly.backend.service.EmailService;
import com.poly.backend.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private OrderDAO orderDAO;
    @Autowired private OrderStatusDAO orderStatusDAO;
    @Autowired private CartDAO cartDAO;
    @Autowired private ProductDAO productDAO;
    @Autowired private ProductVariantDAO variantDAO;
    @Autowired private VoucherDAO voucherDAO;
    @Autowired private UserDAO userDAO;
    @Autowired private EmailService emailService;
    @Autowired private CartItemDAO cartItemDAO;

    @Override
    @Transactional
    public OrderResponseDTO placeOrder(Integer userId, OrderRequestDTO request) {

        User customer = userDAO.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy User"));
        Cart cart = cartDAO.findByUser_UserId(userId).orElse(null);

        Order order = new Order();
        order.setCustomer(customer);
        order.setNote(request.getNote());

        // Lấy thời gian (Năm/Tháng/Ngày/Giờ/Phút/Giây) -> VD: 20260324143045
        String dateStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Ép cứng KHÔNG CÓ DẤU GẠCH NGANG (VD: TZ20260324143045)
        order.setOrderCode("TZ" + dateStamp + String.format("%04d", (int)(Math.random() * 10000)));

        OrderStatus status = orderStatusDAO.findById(0).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống"));
        order.setStatus(status);

        BigDecimal totalMoney = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            List<CartItem> itemsToRemove = new ArrayList<>();

            for (GuestCartItemDTO reqItem : request.getItems()) {
                Product product = productDAO.findById(reqItem.getProductId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm không tồn tại"));

                ProductVariant variant = null;
                if (reqItem.getVariantId() != null) {
                    variant = variantDAO.findById(reqItem.getVariantId()).orElse(null);
                }

                // TRỪ TỒN KHO CHÍNH XÁC VÀO BIẾN THỂ
                if (variant != null) {
                    if (variant.getStockQuantity() < reqItem.getQuantity()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm " + product.getName() + " (" + variant.getColorName() + ") không đủ số lượng");
                    variant.setStockQuantity(variant.getStockQuantity() - reqItem.getQuantity());
                    variantDAO.save(variant);
                } else {
                    // Nếu ko có biến thể (Sản phẩm cũ)
                    if (product.getTotalStock() < reqItem.getQuantity()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm " + product.getName() + " không đủ số lượng");
                    // (Logic bỏ qua vì DB mới bắt buộc có Variant, nhưng cứ để đây phòng hờ)
                }

                // Tính tiền
                BigDecimal originalPrice = variant != null && variant.getPrice() != null && variant.getPrice().compareTo(BigDecimal.ZERO) > 0 ? variant.getPrice() : product.getPrice();
                BigDecimal salePrice = variant != null && variant.getSalePrice() != null && variant.getSalePrice().compareTo(BigDecimal.ZERO) > 0 ? variant.getSalePrice() : product.getSalePrice();
                BigDecimal applyPrice = (salePrice != null && salePrice.compareTo(BigDecimal.ZERO) > 0) ? salePrice : originalPrice;

                totalMoney = totalMoney.add(applyPrice.multiply(new BigDecimal(reqItem.getQuantity())));

                OrderDetail detail = new OrderDetail();
                detail.setOrder(order);
                detail.setProduct(product);
                detail.setVariant(variant); // LƯU VÀO DB
                detail.setQuantity(reqItem.getQuantity());
                detail.setPrice(applyPrice);
                orderDetails.add(detail);

                // Dọn dẹp giỏ hàng
                if (cart != null && cart.getCartItems() != null) {
                    final Integer targetVId = variant != null ? variant.getVariantId() : null;
                    cart.getCartItems().stream()
                            .filter(i -> i.getProduct().getProductId().equals(reqItem.getProductId()) &&
                                    (i.getVariant() == null ? targetVId == null : i.getVariant().getVariantId().equals(targetVId)))
                            .findFirst()
                            .ifPresent(itemsToRemove::add);
                }
            }

            if (cart != null && !itemsToRemove.isEmpty()) {
                cart.getCartItems().removeAll(itemsToRemove);
                cartItemDAO.deleteAll(itemsToRemove);
            }

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giỏ hàng đang trống hoặc lỗi truyền dữ liệu!");
        }

        order.setTotalMoney(totalMoney);
        order.setOrderDetails(orderDetails);

        // Voucher logic... (Giữ nguyên)
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (request.getVoucherCode() != null && !request.getVoucherCode().trim().isEmpty()) {
            Voucher voucher = voucherDAO.findByCode(request.getVoucherCode()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá không hợp lệ"));
            if (!voucher.getStatus() || voucher.getQuantity() <= 0 || voucher.getEndDate().isBefore(LocalDateTime.now())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá hết hạn");
            if (totalMoney.compareTo(voucher.getMinOrderValue()) < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chưa đạt giá trị tối thiểu");
            discountAmount = voucher.getDiscountAmount();
            order.setVoucher(voucher);
            voucher.setQuantity(voucher.getQuantity() - 1);
            voucherDAO.save(voucher);
        }

        order.setDiscountAmount(discountAmount);
        BigDecimal finalAmount = totalMoney.subtract(discountAmount);
        order.setFinalAmount(finalAmount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : finalAmount);
        order.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "COD");
//        order.setPaymentStatus(false);
        order.setPaymentStatus(request.getIsPaid() != null ? request.getIsPaid() : false);
        Order savedOrder = orderDAO.save(order);
        emailService.sendOrderConfirmation(request.getEmail() != null ? request.getEmail() : customer.getEmail(), savedOrder.getOrderCode(), savedOrder.getFinalAmount().toString());

        return mapToDTO(savedOrder);
    }

    @Override
    public List<OrderResponseDTO> getOrderHistory(Integer userId) {
        return orderDAO.findByCustomer_UserIdOrderByOrderDateDesc(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private OrderResponseDTO mapToDTO(Order order) {
        List<OrderDetailResponseDTO> detailDTOs = order.getOrderDetails().stream().map(detail -> {
            Product p = detail.getProduct();
            ProductVariant v = detail.getVariant();
            String imgUrl = (v != null && v.getImageUrl() != null && !v.getImageUrl().isEmpty()) ? v.getImageUrl() : p.getImageUrl();

            return OrderDetailResponseDTO.builder()
                    .productId(p.getProductId())
                    .variantId(v != null ? v.getVariantId() : null)
                    .productName(p.getName())
                    .colorName(v != null ? v.getColorName() : null)
                    .option2Value(v != null ? v.getOption2Value() : null)
                    .imageUrl(imgUrl)
                    .quantity(detail.getQuantity())
                    .price(detail.getPrice())
                    .subTotal(detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                    .build();
        }).collect(Collectors.toList());

        String cName = (order.getCustomer() != null) ? order.getCustomer().getFullName() : "Khách vãng lai";
        String sName = (order.getShipper() != null) ? order.getShipper().getFullName() : null;
        String eName = (order.getEmployee() != null) ? order.getEmployee().getFullName() : null;

        return OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .orderCode(order.getOrderCode())
                .orderDate(order.getOrderDate())
                .totalMoney(order.getTotalMoney())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .note(order.getNote())
                .statusId(order.getStatus() != null ? order.getStatus().getStatusId() : 0)
                .statusName(order.getStatus() != null ? order.getStatus().getStatusName() : "Unknown")
                .customerName(cName)
                .shipperName(sName)
                .employeeName(eName)
                .orderDetails(detailDTOs)
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .build();
    }

    @Override
    @Transactional
    public OrderResponseDTO placeGuestOrder(GuestOrderRequestDTO request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giỏ hàng đang trống!");
        }

        Order order = new Order();
        order.setCustomer(null);
        order.setNote(request.getNote());
        // Lấy thời gian (Năm/Tháng/Ngày
        String dateStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Ép cứng KHÔNG CÓ DẤU GẠCH NGANG (VD: TZ20260324143045)
        order.setOrderCode("TZ" + dateStamp + String.format("%04d", (int)(Math.random() * 10000)));
        OrderStatus status = orderStatusDAO.findById(0).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi trạng thái"));
        order.setStatus(status);

        BigDecimal totalMoney = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (GuestCartItemDTO reqItem : request.getItems()) {
            Product product = productDAO.findById(reqItem.getProductId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy SP"));
            ProductVariant variant = null;
            if (reqItem.getVariantId() != null) {
                variant = variantDAO.findById(reqItem.getVariantId()).orElse(null);
            }

            if (variant != null) {
                if (variant.getStockQuantity() < reqItem.getQuantity()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm " + product.getName() + " không đủ số lượng");
                variant.setStockQuantity(variant.getStockQuantity() - reqItem.getQuantity());
                variantDAO.save(variant);
            }

            BigDecimal originalPrice = variant != null && variant.getPrice() != null && variant.getPrice().compareTo(BigDecimal.ZERO) > 0 ? variant.getPrice() : product.getPrice();
            BigDecimal salePrice = variant != null && variant.getSalePrice() != null && variant.getSalePrice().compareTo(BigDecimal.ZERO) > 0 ? variant.getSalePrice() : product.getSalePrice();
            BigDecimal applyPrice = (salePrice != null && salePrice.compareTo(BigDecimal.ZERO) > 0) ? salePrice : originalPrice;

            totalMoney = totalMoney.add(applyPrice.multiply(BigDecimal.valueOf(reqItem.getQuantity())));

            orderDetails.add(OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .variant(variant) // LƯU VÀO DB
                    .quantity(reqItem.getQuantity())
                    .price(applyPrice)
                    .build());
        }

        order.setTotalMoney(totalMoney);
        order.setOrderDetails(orderDetails);

        BigDecimal discountAmount = BigDecimal.ZERO;
        if (request.getVoucherCode() != null && !request.getVoucherCode().trim().isEmpty()) {
            Voucher voucher = voucherDAO.findByCode(request.getVoucherCode()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá không hợp lệ"));
            if (!voucher.getStatus() || voucher.getQuantity() <= 0 || voucher.getEndDate().isBefore(LocalDateTime.now())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá đã hết hạn");
            if (totalMoney.compareTo(voucher.getMinOrderValue()) < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chưa đạt giá trị tối thiểu");
            discountAmount = voucher.getDiscountAmount();
            order.setVoucher(voucher);
            voucher.setQuantity(voucher.getQuantity() - 1);
            voucherDAO.save(voucher);
        }

        order.setDiscountAmount(discountAmount);
        BigDecimal finalAmount = totalMoney.subtract(discountAmount);
        order.setFinalAmount(finalAmount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : finalAmount);
        order.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "COD");
//        order.setPaymentStatus(false);
        // Nhận trạng thái "Đã thanh toán" từ Frontend truyền lên
        order.setPaymentStatus(request.getIsPaid() != null ? request.getIsPaid() : false);

        Order savedOrder = orderDAO.save(order);
        if (request.getGuestEmail() != null && !request.getGuestEmail().isEmpty()) {
            emailService.sendOrderConfirmation(request.getGuestEmail(), savedOrder.getOrderCode(), savedOrder.getFinalAmount().toString());
        }

        return mapToDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO getOrderByCode(String orderCode) {
        Order order = orderDAO.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng: " + orderCode));
        return mapToDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getRecentOrders(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);

        List<Order> orders = orderDAO.findRecentOrdersBetween(sevenDaysAgo, now, pageable);

        return orders.stream().map(order -> {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setOrderId(order.getOrderId());
            dto.setOrderCode(order.getOrderCode());
            dto.setOrderDate(order.getOrderDate());
            dto.setFinalAmount(order.getFinalAmount());
            dto.setTotalMoney(order.getTotalMoney());

            if (order.getStatus() != null) {
                dto.setStatusName(order.getStatus().getStatusName());
            }

            if (order.getCustomer() != null) {
                dto.setCustomerName(order.getCustomer().getFullName());
            } else {
                dto.setCustomerName("Khách vãng lai");
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDTO> getAllOrdersForAdmin() {
        return orderDAO.findAll(Sort.by(Sort.Direction.DESC, "orderDate")).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(Integer orderId, Integer newStatusId, Integer employeeId, Integer shipperId) {
        Order order = orderDAO.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng"));
        OrderStatus status = orderStatusDAO.findById(newStatusId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trạng thái không hợp lệ"));

        if (newStatusId == 1 && employeeId != null) order.setEmployee(userDAO.findById(employeeId).orElse(null));
        if (newStatusId == 2 && shipperId != null) order.setShipper(userDAO.findById(shipperId).orElse(null));

        // NẾU HỦY ĐƠN: HOÀN TRẢ TỒN KHO VÀO ĐÚNG BIẾN THỂ ĐÓ
        if (order.getStatus().getStatusId() != 4 && newStatusId == 4) {
            for (OrderDetail detail : order.getOrderDetails()) {
                if (detail.getVariant() != null) {
                    ProductVariant v = detail.getVariant();
                    v.setStockQuantity(v.getStockQuantity() + detail.getQuantity());
                    variantDAO.save(v);
                }
            }
        }

        order.setStatus(status);
        return mapToDTO(orderDAO.save(order));
    }

    @Override
    public List<OrderResponseDTO> getOrdersForShipper(Integer shipperId) {
        List<Order> allRelevantOrders = orderDAO.findByStatus_StatusIdInOrderByOrderDateAsc(Arrays.asList(1, 2, 3));

        List<OrderResponseDTO> result = new ArrayList<>();

        for (Order order : allRelevantOrders) {
            int status = order.getStatus().getStatusId();

            if (status == 1) {
                result.add(mapToDTO(order));
            }
            else if ((status == 2 || status == 3) && order.getShipper() != null && order.getShipper().getUserId().equals(shipperId)) {
                result.add(mapToDTO(order));
            }
        }

        return result;
    }

    @Override
    @Transactional
    public OrderResponseDTO updatePaymentStatus(Integer orderId, Boolean status) {
        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng"));
        order.setPaymentStatus(status);
        return mapToDTO(orderDAO.save(order));
    }
}