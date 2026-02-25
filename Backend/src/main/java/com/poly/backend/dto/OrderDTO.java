package com.poly.backend.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Integer orderId;

    // --- INPUT FIELDS (Dữ liệu Frontend có thể gửi lên) ---

    @Size(max = 20, message = "Mã đơn hàng không được quá 20 ký tự")
    private String orderCode; // Backend có thể tự sinh, hoặc Frontend gửi nếu có logic riêng

    @Size(max = 500, message = "Ghi chú không được quá 500 ký tự")
    private String note;

    private Integer voucherId; // Mã Voucher khách áp dụng (nếu có)

    private Integer customerId; // Khách hàng mua (Thường lấy từ Token, nhưng để đây cho chắc)

    // --- CALCULATED FIELDS (Backend tự tính, Frontend chỉ hiển thị) ---
    // Không thêm Validation @NotNull vì lúc tạo đơn, Frontend chưa biết các số này

    private BigDecimal totalMoney;    // Tổng tiền hàng
    private BigDecimal discountAmount;// Tiền được giảm
    private BigDecimal finalAmount;   // Khách phải trả
    private LocalDateTime orderDate;  // Ngày tạo đơn

    // --- FLATTENED OUTPUT FIELDS (Dữ liệu để hiển thị lên UI) ---

    // 1. Thông tin trạng thái
    private Integer statusId;
    private String statusName;      // Ví dụ: "Đang giao hàng"
    private String statusDescription;

    // 2. Thông tin khách hàng (Để in hóa đơn hoặc Admin xem)
    private String customerName;
    private String customerPhone;
    private String customerAddress;

    // 3. Thông tin nhân viên (Người duyệt đơn - nếu có)
    private Integer employeeId;
    private String employeeName;

    // 4. Thông tin Voucher (Để hiển thị lại mã đã dùng)
    private String voucherCode;

    private List<OrderDetailDTO> orderDetails;
}