package com.poly.backend.controller;

import com.poly.backend.dao.VoucherDAO;
import com.poly.backend.entity.Voucher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherDAO voucherDAO;

    @GetMapping("/check")
    public ResponseEntity<?> checkVoucher(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "orderValue", required = false) String orderValueStr) {

        // 1. Kiểm tra nếu thiếu tham số truyền vào từ Postman
        if (code == null || code.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Vui lòng nhập mã giảm giá (code).");
        }
        if (orderValueStr == null || orderValueStr.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Vui lòng nhập giá trị đơn hàng (orderValue).");
        }

        try {
            // 2. Tự tay chuyển đổi String sang BigDecimal để tránh lỗi "converted to null"
            // trim() để loại bỏ khoảng trắng dư thừa nếu bạn lỡ tay gõ vào Postman
            BigDecimal orderValue = new BigDecimal(orderValueStr.trim());

            return voucherDAO.findByCode(code.trim())
                    .map(v -> {
                        LocalDateTime now = LocalDateTime.now();

                        // Kiểm tra trạng thái và thời gian hiệu lực
                        if (v.getStatus() == null || !v.getStatus()
                                || now.isBefore(v.getStartDate())
                                || now.isAfter(v.getEndDate())) {
                            return ResponseEntity.badRequest().body("Mã giảm giá đã hết hạn hoặc không hoạt động.");
                        }

                        // Kiểm tra số lượng còn lại
                        if (v.getQuantity() != null && v.getQuantity() <= 0) {
                            return ResponseEntity.badRequest().body("Mã giảm giá đã hết lượt sử dụng.");
                        }

                        // Kiểm tra giá trị đơn hàng tối thiểu
                        if (v.getMinOrderValue() != null && orderValue.compareTo(v.getMinOrderValue()) < 0) {
                            return ResponseEntity.badRequest().body("Đơn hàng chưa đạt giá trị tối thiểu " + v.getMinOrderValue());
                        }

                        // Hợp lệ -> Trả về số tiền được giảm
                        return ResponseEntity.ok(v.getDiscountAmount());
                    })
                    .orElse(ResponseEntity.status(404).body("Mã giảm giá không tồn tại."));

        } catch (NumberFormatException e) {
            // Trả về lỗi nếu người dùng nhập chữ vào ô orderValue thay vì nhập số
            return ResponseEntity.badRequest().body("Giá trị đơn hàng không hợp lệ (phải là số).");
        }
    }
}