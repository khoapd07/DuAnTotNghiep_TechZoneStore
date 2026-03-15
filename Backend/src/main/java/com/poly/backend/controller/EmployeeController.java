package com.poly.backend.controller;

import com.poly.backend.dto.EmployeeDTO;
import com.poly.backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/employees")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO dto) {
        // Đảm bảo UserId là null để Service hiểu đây là thêm mới
        dto.setUserId(null);
        return ResponseEntity.ok(employeeService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Integer id, @RequestBody EmployeeDTO dto) {
        dto.setUserId(id);
        EmployeeDTO updated = employeeService.save(dto);
        if(updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<String> toggleStatus(@PathVariable Integer id) {
        String res = employeeService.toggleStatus(id);
        if(res == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(res);
    }
}