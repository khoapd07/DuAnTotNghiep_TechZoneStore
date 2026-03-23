package com.poly.backend.controller;

import com.poly.backend.dto.ImportReceiptDetailDTO;
import com.poly.backend.dto.ImportReceiptListDTO;
import com.poly.backend.entity.ImportReceipt;
import com.poly.backend.service.ImportReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j // Khai báo Logger
@CrossOrigin(origins = "*") // Lưu ý: Nên giới hạn domain cụ thể khi lên production
@RestController
@RequestMapping("/api/admin/import-receipts")
@RequiredArgsConstructor // Tự động tạo constructor cho các biến final (Constructor Injection)
public class ImportReceiptController {

    private final ImportReceiptService importReceiptService;

    // 1. LẤY DANH SÁCH
    @GetMapping
    public ResponseEntity<List<ImportReceiptListDTO>> getAll() {
        return ResponseEntity.ok(importReceiptService.getAllReceiptsForList());
    }

    // 2. LẤY CHI TIẾT THEO ID
    @GetMapping("/{id}")
    public ResponseEntity<ImportReceiptDetailDTO> getById(@PathVariable Integer id) {
        ImportReceiptDetailDTO dto = importReceiptService.getReceiptDetail(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    // 3. TẠO MỚI PHIẾU NHẬP
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ImportReceipt receipt) {
        try {
            // Lý tưởng nhất là thay ImportReceipt bằng ImportReceiptRequestDTO
            ImportReceipt savedReceipt = importReceiptService.createReceipt(receipt);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReceipt);
        } catch (Exception e) {
            log.error("Lỗi khi tạo mới phiếu nhập: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể tạo phiếu nhập: " + e.getMessage());
        }
    }

    // 4. CẬP NHẬT PHIẾU NHẬP
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ImportReceipt receipt) {
        try {
            ImportReceipt updatedReceipt = importReceiptService.updateReceipt(id, receipt);
            if (updatedReceipt != null) {
                return ResponseEntity.ok(updatedReceipt);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật phiếu nhập ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể cập nhật: " + e.getMessage());
        }
    }

    // 5. HỦY / XÓA PHIẾU NHẬP
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            importReceiptService.cancelReceipt(id);
            return ResponseEntity.ok().body("Đã hủy phiếu nhập thành công");
        } catch (Exception e) {
            log.error("Lỗi khi hủy phiếu nhập ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể hủy: " + e.getMessage());
        }
    }
}