package com.poly.backend.controller;

import com.poly.backend.dto.CustomerDTO;
import com.poly.backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/customers")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomerController {

    private final CustomerService customerService;

    // 1. Lấy danh sách khách hàng
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.findAll());
    }

    // 2. Lấy chi tiết khách hàng
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Integer id) {
        CustomerDTO customerDTO = customerService.findById(id);
        if(customerDTO == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(customerDTO);
    }

    // THÊM MỚI: API Tạo khách hàng
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDTO dto) {
        try {
            return ResponseEntity.ok(customerService.create(dto));
        } catch (IllegalArgumentException e) {
            // Trả về mã lỗi 400 và câu thông báo tiếng Việt
            return ResponseEntity.badRequest().body(java.util.Map.of("message", e.getMessage()));
        }
    }

    // 3. Cập nhật trạng thái
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<String> toggleStatus(@PathVariable Integer id) {
        String result = customerService.toggleStatus(id);
        if(result == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    // 4. Cập nhật thông tin khách hàng
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer id, @RequestBody CustomerDTO dto) {
        try {
            dto.setUserId(id);
            CustomerDTO updatedDto = customerService.save(dto);
            return ResponseEntity.ok(updatedDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", e.getMessage()));
        }
    }
    // THÊM MỚI: API Xóa khách hàng
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteById(id);
        return ResponseEntity.ok("Xóa thành công");
    }
}