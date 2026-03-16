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
    @Transactional // Rất quan trọng: Bị lỗi ở bất kỳ bước nào là Rollback toàn bộ
    public OrderResponseDTO placeOrder(Integer userId, OrderRequestDTO request) {

        User customer = userDAO.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy User"));

        // LẤY GIỎ HÀNG TỪ DATABASE RA TRƯỚC KHI LÀM BẤT CỨ VIỆC GÌ
        Cart cart = cartDAO.findByUser_UserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy giỏ hàng"));

        // 1. Tạo đơn hàng mới
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

        // 2. Xử lý từng món hàng, Tính tiền & Trừ tồn kho
        BigDecimal totalMoney = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();

        // NẾU FRONTEND TRUYỀN LÊN DANH SÁCH (Dùng cho "Mua lại" hoặc "Thanh toán từ Checkout")
        if (request.getItems() != null && !request.getItems().isEmpty()) {

            // Tạo 1 list ảo để chứa các món cần xóa (Tránh lỗi xóa trực tiếp khi đang lặp)
            List<CartItem> itemsToRemove = new ArrayList<>();

            for (GuestCartItemDTO requestItem : request.getItems()) {
                // 1. Tìm xem món khách tick chọn có thật sự nằm trong giỏ hàng DB không
                CartItem cartItem = cart.getCartItems().stream()
                        .filter(i -> i.getProduct().getProductId().equals(requestItem.getProductId()))
                        .findFirst()
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm không có trong giỏ hàng"));

                Product product = cartItem.getProduct();

                // Kiểm tra tồn kho
                if (product.getStockQuantity() < requestItem.getQuantity()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm " + product.getName() + " không đủ số lượng");
                }

                // Trừ tồn kho
                product.setStockQuantity(product.getStockQuantity() - requestItem.getQuantity());
                productDAO.save(product);

                // Tính tiền
                BigDecimal price = product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();
                totalMoney = totalMoney.add(price.multiply(new BigDecimal(requestItem.getQuantity())));

                // Tạo OrderDetail
                OrderDetail detail = new OrderDetail();
                detail.setOrder(order);
                detail.setProduct(product);
                detail.setQuantity(requestItem.getQuantity()); // Lấy số lượng khách chọn
                detail.setPrice(price);
                orderDetails.add(detail);

                // Thêm vào danh sách chờ xóa khỏi giỏ
                itemsToRemove.add(cartItem);
            }

            // XÓA NHỮNG MÓN ĐÃ MUA KHỎI GIỎ HÀNG AN TOÀN
            cart.getCartItems().removeAll(itemsToRemove);
            cartItemDAO.deleteAll(itemsToRemove);

        } else {
            // GIỮ LẠI LOGIC CŨ LÀM DỰ PHÒNG (Trường hợp Frontend không truyền items)
            if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giỏ hàng đang trống!");
            }

            for (CartItem cartItem : cart.getCartItems()) {
                Product product = cartItem.getProduct();
                if (product.getStockQuantity() < cartItem.getQuantity()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm " + product.getName() + " không đủ số lượng");
                }

                product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
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
            // Xóa sạch giỏ hàng DB
            cart.getCartItems().clear();
            cartItemDAO.deleteAll(cart.getCartItems());
        }

        order.setTotalMoney(totalMoney);
        order.setOrderDetails(orderDetails); // Gắn danh sách chi tiết vào đơn hàng

        // 3. Áp dụng Voucher (Nếu khách có nhập)
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

        order.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "COD");
        order.setPaymentStatus(false);

        // 4. Lưu hóa đơn vào Database
        Order savedOrder = orderDAO.save(order);

        // Gửi email xác nhận
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

        // Xác định tên khách hàng
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
    @Transactional // Vẫn phải có Transactional để Rollback nếu lỗi
    public OrderResponseDTO placeGuestOrder(GuestOrderRequestDTO request) {

        // 1. Kiểm tra Giỏ hàng từ Request gửi lên có rỗng không
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giỏ hàng đang trống!");
        }

        // 2. Tạo đơn hàng mới (KHÔNG CÓ CUSTOMER)
        Order order = new Order();
        order.setCustomer(null); // Khách vãng lai nên User = null

        // Ghi chú đã được Frontend nối chuỗi (Gồm tên, sđt, địa chỉ người nhận)
        order.setNote(request.getNote());

        // Random mã đơn hàng riêng cho Guest (VD: TZ-GUEST-20260302-XXXX)
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        order.setOrderCode("TZ-G-" + timeStamp + "-" + (int)(Math.random() * 10000));

        // Mặc định trạng thái là 0 (Pending)
        OrderStatus status = orderStatusDAO.findById(0)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi trạng thái đơn hàng"));
        order.setStatus(status);

        // 3. Xử lý từng món hàng, Tính tiền & Trừ tồn kho
        BigDecimal totalMoney = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (GuestCartItemDTO item : request.getItems()) {
            // Tìm sản phẩm trong DB dựa trên ID mà Frontend gửi lên
            Product currentProduct = productDAO.findById(item.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm ID: " + item.getProductId()));

            // Kiểm tra tồn kho
            if (currentProduct.getStockQuantity() < item.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Sản phẩm '" + currentProduct.getName() + "' chỉ còn " + currentProduct.getStockQuantity() + " chiếc!");
            }

            // Trừ tồn kho
            currentProduct.setStockQuantity(currentProduct.getStockQuantity() - item.getQuantity());
            productDAO.save(currentProduct);

            // Chốt giá bán (Luôn lấy giá DB cho an toàn, không tin tưởng giá từ Frontend gửi lên hoàn toàn)
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

            order.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "COD");
            order.setPaymentStatus(false);// Mặc định tạo đơn xong là chưa thanh toán

            // Trừ lượt dùng voucher
            voucher.setQuantity(voucher.getQuantity() - 1);
            voucherDAO.save(voucher);
        }

        order.setDiscountAmount(discountAmount);

        // Tính tiền thực trả (Không để âm tiền)
        BigDecimal finalAmount = totalMoney.subtract(discountAmount);
        order.setFinalAmount(finalAmount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : finalAmount);

        order.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "COD");
        order.setPaymentStatus(false);

        // 5. Lưu hóa đơn vào Database
        Order savedOrder = orderDAO.save(order);

        // Chú ý: Không cần bước xóa Giỏ hàng DB như User, vì Khách vãng lai xóa giỏ hàng ở dưới Frontend (localStorage) rồi

        if (request.getGuestEmail() != null && !request.getGuestEmail().isEmpty()) {
            emailService.sendOrderConfirmation(request.getGuestEmail(), savedOrder.getOrderCode(), savedOrder.getFinalAmount().toString());
        }

        return mapToDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO getOrderByCode(String orderCode) {
        Order order = orderDAO.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng: " + orderCode));
        return mapToDTO(order); // Hàm mapToDTO đã có sẵn ở dưới file của bạn
    }


    @Override
    public List<OrderResponseDTO> getRecentOrders(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        // Tính mốc 7 ngày
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);

        // Gọi hàm DAO mới tạo ở Bước 1
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

            // Logic lấy tên khách hàng (đã sửa ở lần trước)
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
        // Lấy tất cả đơn hàng, sắp xếp mới nhất lên đầu
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

        // Chỉ cộng lại nếu trạng thái cũ KHÔNG PHẢI là hủy, và trạng thái mới LÀ hủy (id = 4)
        if (order.getStatus().getStatusId() != 4 && newStatusId == 4) {
            for (OrderDetail detail : order.getOrderDetails()) {
                Product p = detail.getProduct();
                p.setStockQuantity(p.getStockQuantity() + detail.getQuantity());
                productDAO.save(p);
            }
        }

        order.setStatus(status);
        Order savedOrder = orderDAO.save(order);

        return mapToDTO(savedOrder);
    }

    @Override
    public List<OrderResponseDTO> getOrdersForShipper(Integer shipperId) {
        // Lấy tất cả đơn có status 1, 2, 3
        List<Order> allRelevantOrders = orderDAO.findByStatus_StatusIdInOrderByOrderDateAsc(Arrays.asList(1, 2, 3));

        List<OrderResponseDTO> result = new ArrayList<>();

        for (Order order : allRelevantOrders) {
            int status = order.getStatus().getStatusId();

            // Nếu là đơn CHỜ NHẬN (1) -> Ai cũng được xem
            if (status == 1) {
                result.add(mapToDTO(order));
            }
            // Nếu là đơn ĐANG GIAO (2) hoặc HOÀN THÀNH (3) -> Chỉ Shipper ĐÃ NHẬN đơn đó mới được xem
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