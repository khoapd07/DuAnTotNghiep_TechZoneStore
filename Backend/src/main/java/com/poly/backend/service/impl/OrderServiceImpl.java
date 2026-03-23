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
    @Autowired private VoucherDAO voucherDAO;
    @Autowired private UserDAO userDAO;
    @Autowired private EmailService emailService;
    @Autowired private CartItemDAO cartItemDAO;

    @Override
    @Transactional
    public OrderResponseDTO placeOrder(Integer userId, OrderRequestDTO request) {

        User customer = userDAO.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy User"));

        Cart cart = cartDAO.findByUser_UserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy giỏ hàng"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setNote(request.getNote());

        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        order.setOrderCode("TZ-" + timeStamp + "-" + (int)(Math.random() * 10000));

        OrderStatus status = orderStatusDAO.findById(0)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi trạng thái đơn hàng"));
        order.setStatus(status);

        BigDecimal totalMoney = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            List<CartItem> itemsToRemove = new ArrayList<>();

            for (GuestCartItemDTO requestItem : request.getItems()) {
                CartItem cartItem = cart.getCartItems().stream()
                        .filter(i -> i.getProduct().getProductId().equals(requestItem.getProductId()))
                        .findFirst()
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm không có trong giỏ hàng"));

                Product product = cartItem.getProduct();

                // ĐÃ SỬA: Dùng getTotalStock() để kiểm tra
                if (product.getTotalStock() < requestItem.getQuantity()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm " + product.getName() + " không đủ số lượng");
                }

                // ĐÃ SỬA: Logic trừ lùi tồn kho vào các biến thể (Vì chưa có variant_id trong CartItem)
                int qtyToDeduct = requestItem.getQuantity();
                for (ProductVariant variant : product.getVariants()) {
                    if (qtyToDeduct <= 0) break;
                    if (variant.getStockQuantity() > 0) {
                        int deduct = Math.min(variant.getStockQuantity(), qtyToDeduct);
                        variant.setStockQuantity(variant.getStockQuantity() - deduct);
                        qtyToDeduct -= deduct;
                    }
                }
                productDAO.save(product);

                BigDecimal price = product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();
                totalMoney = totalMoney.add(price.multiply(new BigDecimal(requestItem.getQuantity())));

                OrderDetail detail = new OrderDetail();
                detail.setOrder(order);
                detail.setProduct(product);
                detail.setQuantity(requestItem.getQuantity());
                detail.setPrice(price);
                orderDetails.add(detail);

                itemsToRemove.add(cartItem);
            }
            cart.getCartItems().removeAll(itemsToRemove);
            cartItemDAO.deleteAll(itemsToRemove);

        } else {
            if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giỏ hàng đang trống!");
            }

            for (CartItem cartItem : cart.getCartItems()) {
                Product product = cartItem.getProduct();
                if (product.getTotalStock() < cartItem.getQuantity()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm " + product.getName() + " không đủ số lượng");
                }

                // ĐÃ SỬA: Trừ lùi tồn kho biến thể
                int qtyToDeduct = cartItem.getQuantity();
                for (ProductVariant variant : product.getVariants()) {
                    if (qtyToDeduct <= 0) break;
                    if (variant.getStockQuantity() > 0) {
                        int deduct = Math.min(variant.getStockQuantity(), qtyToDeduct);
                        variant.setStockQuantity(variant.getStockQuantity() - deduct);
                        qtyToDeduct -= deduct;
                    }
                }
                productDAO.save(product);

                BigDecimal price = product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();
                totalMoney = totalMoney.add(price.multiply(new BigDecimal(cartItem.getQuantity())));

                OrderDetail detail = new OrderDetail();
                detail.setOrder(order);
                detail.setProduct(product);
                detail.setQuantity(cartItem.getQuantity());
                detail.setPrice(price);
                orderDetails.add(detail);
            }
            cart.getCartItems().clear();
            cartItemDAO.deleteAll(cart.getCartItems());
        }

        order.setTotalMoney(totalMoney);
        order.setOrderDetails(orderDetails);

        BigDecimal discountAmount = BigDecimal.ZERO;
        if (request.getVoucherCode() != null && !request.getVoucherCode().trim().isEmpty()) {
            Voucher voucher = voucherDAO.findByCode(request.getVoucherCode())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá không hợp lệ"));

            if (!voucher.getStatus() || voucher.getQuantity() <= 0 || voucher.getEndDate().isBefore(LocalDateTime.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá đã hết hạn hoặc hết lượt dùng");
            }
            if (totalMoney.compareTo(voucher.getMinOrderValue()) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đơn hàng chưa đạt giá trị tối thiểu để dùng mã này");
            }

            discountAmount = voucher.getDiscountAmount();
            order.setVoucher(voucher);

            voucher.setQuantity(voucher.getQuantity() - 1);
            voucherDAO.save(voucher);
        }

        order.setDiscountAmount(discountAmount);

        BigDecimal finalAmount = totalMoney.subtract(discountAmount);
        order.setFinalAmount(finalAmount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : finalAmount);

        order.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "COD");
        order.setPaymentStatus(false);

        Order savedOrder = orderDAO.save(order);

        String sendToEmail = (request.getEmail() != null && !request.getEmail().isEmpty())
                ? request.getEmail() : customer.getEmail();
        emailService.sendOrderConfirmation(sendToEmail, savedOrder.getOrderCode(), savedOrder.getFinalAmount().toString());

        return mapToDTO(savedOrder);
    }

    @Override
    public List<OrderResponseDTO> getOrderHistory(Integer userId) {
        return orderDAO.findByCustomer_UserIdOrderByOrderDateDesc(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

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

        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        order.setOrderCode("TZ-G-" + timeStamp + "-" + (int)(Math.random() * 10000));

        OrderStatus status = orderStatusDAO.findById(0)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi trạng thái đơn hàng"));
        order.setStatus(status);

        BigDecimal totalMoney = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (GuestCartItemDTO item : request.getItems()) {
            Product currentProduct = productDAO.findById(item.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm ID: " + item.getProductId()));

            // ĐÃ SỬA: Kiểm tra tồn kho tổng
            if (currentProduct.getTotalStock() < item.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Sản phẩm '" + currentProduct.getName() + "' chỉ còn " + currentProduct.getTotalStock() + " chiếc!");
            }

            // ĐÃ SỬA: Trừ lùi tồn kho biến thể
            int qtyToDeduct = item.getQuantity();
            for (ProductVariant variant : currentProduct.getVariants()) {
                if (qtyToDeduct <= 0) break;
                if (variant.getStockQuantity() > 0) {
                    int deduct = Math.min(variant.getStockQuantity(), qtyToDeduct);
                    variant.setStockQuantity(variant.getStockQuantity() - deduct);
                    qtyToDeduct -= deduct;
                }
            }
            productDAO.save(currentProduct);

            BigDecimal applyPrice = (currentProduct.getSalePrice() != null && currentProduct.getSalePrice().compareTo(BigDecimal.ZERO) > 0)
                    ? currentProduct.getSalePrice() : currentProduct.getPrice();

            totalMoney = totalMoney.add(applyPrice.multiply(BigDecimal.valueOf(item.getQuantity())));

            orderDetails.add(OrderDetail.builder()
                    .order(order)
                    .product(currentProduct)
                    .quantity(item.getQuantity())
                    .price(applyPrice)
                    .build());
        }

        order.setTotalMoney(totalMoney);
        order.setOrderDetails(orderDetails);

        BigDecimal discountAmount = BigDecimal.ZERO;
        if (request.getVoucherCode() != null && !request.getVoucherCode().trim().isEmpty()) {
            Voucher voucher = voucherDAO.findByCode(request.getVoucherCode())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá không hợp lệ"));

            if (!voucher.getStatus() || voucher.getQuantity() <= 0 || voucher.getEndDate().isBefore(LocalDateTime.now())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã giảm giá đã hết hạn hoặc hết lượt dùng");
            }
            if (totalMoney.compareTo(voucher.getMinOrderValue()) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đơn hàng chưa đạt giá trị tối thiểu để dùng mã này");
            }

            discountAmount = voucher.getDiscountAmount();
            order.setVoucher(voucher);

            voucher.setQuantity(voucher.getQuantity() - 1);
            voucherDAO.save(voucher);
        }

        order.setDiscountAmount(discountAmount);

        BigDecimal finalAmount = totalMoney.subtract(discountAmount);
        order.setFinalAmount(finalAmount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : finalAmount);

        order.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "COD");
        order.setPaymentStatus(false);

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
        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng"));

        OrderStatus status = orderStatusDAO.findById(newStatusId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trạng thái không hợp lệ"));

        if (newStatusId == 1 && employeeId != null) {
            User employee = userDAO.findById(employeeId).orElse(null);
            order.setEmployee(employee);
        }

        if (newStatusId == 2 && shipperId != null) {
            User shipper = userDAO.findById(shipperId).orElse(null);
            order.setShipper(shipper);
        }

        // ĐÃ SỬA: Hoàn trả tồn kho nếu hủy đơn hàng
        if (order.getStatus().getStatusId() != 4 && newStatusId == 4) {
            for (OrderDetail detail : order.getOrderDetails()) {
                Product p = detail.getProduct();
                // Trả tạm tồn kho vào biến thể đầu tiên
                if (p.getVariants() != null && !p.getVariants().isEmpty()) {
                    ProductVariant v = p.getVariants().get(0);
                    v.setStockQuantity(v.getStockQuantity() + detail.getQuantity());
                }
                productDAO.save(p);
            }
        }

        order.setStatus(status);
        Order savedOrder = orderDAO.save(order);

        return mapToDTO(savedOrder);
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