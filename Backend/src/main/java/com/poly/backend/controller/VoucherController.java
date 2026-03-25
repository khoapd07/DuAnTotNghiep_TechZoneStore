package com.poly.backend.controller;

import com.poly.backend.dao.VoucherDAO;
import com.poly.backend.entity.Voucher;
import com.poly.backend.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherDAO voucherDAO;
    private final VoucherService voucherService;

    @GetMapping
    public ResponseEntity<List<Voucher>> getAllVouchers() {
        return ResponseEntity.ok(voucherService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voucher> getVoucherById(@PathVariable Integer id) {
        Voucher voucher = voucherService.findById(id);
        if (voucher != null) {
            return ResponseEntity.ok(voucher);
        }
        return ResponseEntity.notFound().build();
    }

    // MỚI THÊM: API Lấy Voucher đang hiển thị ở trang chủ
    @GetMapping("/homepage")
    public ResponseEntity<Voucher> getHomepageVoucher() {
        Voucher homepageVoucher = voucherService.findAll().stream()
                .filter(v -> Boolean.TRUE.equals(v.getIsHomepage()) && Boolean.TRUE.equals(v.getStatus()))
                .findFirst()
                .orElse(null);

        if (homepageVoucher != null) {
            return ResponseEntity.ok(homepageVoucher);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher voucher) {
        if (voucher.getCode() != null) {
            voucher.setCode(voucher.getCode().toUpperCase().trim());
        }

        // Nếu set làm trang chủ, phải hủy các voucher trang chủ cũ
        if (Boolean.TRUE.equals(voucher.getIsHomepage())) {
            List<Voucher> allVouchers = voucherService.findAll();
            for (Voucher v : allVouchers) {
                if (Boolean.TRUE.equals(v.getIsHomepage())) {
                    v.setIsHomepage(false);
                    voucherService.save(v);
                }
            }
        }

        return ResponseEntity.ok(voucherService.save(voucher));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable Integer id, @RequestBody Voucher voucherDetails) {
        Voucher existingVoucher = voucherService.findById(id);
        if (existingVoucher != null) {
            existingVoucher.setCode(voucherDetails.getCode().toUpperCase().trim());
            existingVoucher.setName(voucherDetails.getName());
            existingVoucher.setDiscountAmount(voucherDetails.getDiscountAmount());
            existingVoucher.setMinOrderValue(voucherDetails.getMinOrderValue());
            existingVoucher.setQuantity(voucherDetails.getQuantity());
            existingVoucher.setStartDate(voucherDetails.getStartDate());
            existingVoucher.setEndDate(voucherDetails.getEndDate());
            existingVoucher.setStatus(voucherDetails.getStatus());
            existingVoucher.setDescription(voucherDetails.getDescription());
            existingVoucher.setIsHomepage(voucherDetails.getIsHomepage());

            // Nếu set làm trang chủ, phải hủy các voucher trang chủ cũ
            if (Boolean.TRUE.equals(voucherDetails.getIsHomepage())) {
                List<Voucher> allVouchers = voucherService.findAll();
                for (Voucher v : allVouchers) {
                    if (!v.getVoucherId().equals(id) && Boolean.TRUE.equals(v.getIsHomepage())) {
                        v.setIsHomepage(false);
                        voucherService.save(v);
                    }
                }
            }

            return ResponseEntity.ok(voucherService.save(existingVoucher));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Integer id) {
        Voucher existingVoucher = voucherService.findById(id);
        if (existingVoucher != null) {
            voucherService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkVoucher(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "orderValue", required = false) String orderValueStr) {

        if (code == null || code.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Vui lòng nhập mã giảm giá (code).");
        }
        if (orderValueStr == null || orderValueStr.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Vui lòng nhập giá trị đơn hàng (orderValue).");
        }

        try {
            BigDecimal orderValue = new BigDecimal(orderValueStr.trim());

            return voucherDAO.findByCode(code.trim())
                    .map(v -> {
                        LocalDateTime now = LocalDateTime.now();

                        if (v.getStatus() == null || !v.getStatus()
                                || now.isBefore(v.getStartDate())
                                || now.isAfter(v.getEndDate())) {
                            return ResponseEntity.badRequest().body("Mã giảm giá đã hết hạn hoặc không hoạt động.");
                        }

                        if (v.getQuantity() != null && v.getQuantity() <= 0) {
                            return ResponseEntity.badRequest().body("Mã giảm giá đã hết lượt sử dụng.");
                        }

                        if (v.getMinOrderValue() != null && orderValue.compareTo(v.getMinOrderValue()) < 0) {
                            return ResponseEntity.badRequest().body("Đơn hàng chưa đạt giá trị tối thiểu " + v.getMinOrderValue());
                        }

                        // ĐÃ XÓA LOGIC CHẶN NGÀY FLASHSALE THEO YÊU CẦU CỦA BẠN

                        return ResponseEntity.ok(v.getDiscountAmount());
                    })
                    .orElse(ResponseEntity.status(404).body("Mã giảm giá không tồn tại."));

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Giá trị đơn hàng không hợp lệ (phải là số).");
        }
    }
}