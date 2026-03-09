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

    // 3. Cập nhật trạng thái
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<String> toggleStatus(@PathVariable Integer id) {
        String result = customerService.toggleStatus(id);
        if(result == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result);
    }

    // 4. Cập nhật thông tin khách hàng
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Integer id, @RequestBody CustomerDTO dto) {
        // Đảm bảo ID trên URL khớp với ID trong Body
        dto.setUserId(id);
        CustomerDTO updatedDto = customerService.save(dto);

        if (updatedDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updatedDto);
    }
}