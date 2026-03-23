package com.poly.backend.controller;

import com.poly.backend.dto.SupplierDTO;
import com.poly.backend.entity.Supplier;
import com.poly.backend.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    // 1. LẤY DANH SÁCH (Khớp với axios.get)
    @GetMapping
    public ResponseEntity<List<SupplierDTO>> getAll() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    // 2. LẤY CHI TIẾT 1 NHÀ CUNG CẤP (Dự phòng nếu cần)
    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getById(@PathVariable Integer id) {
        Supplier supplier = supplierService.getSupplierById(id);
        if (supplier != null) {
            return ResponseEntity.ok(supplier);
        }
        return ResponseEntity.notFound().build();
    }

    // 3. THÊM MỚI (Khớp với axios.post)
    @PostMapping
    public ResponseEntity<Supplier> create(@RequestBody Supplier supplier) {
        try {
            Supplier savedSupplier = supplierService.createSupplier(supplier);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplier);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 4. CẬP NHẬT (Khớp với axios.put)
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> update(@PathVariable Integer id, @RequestBody Supplier supplier) {
        try {
            Supplier updatedSupplier = supplierService.updateSupplier(id, supplier);
            if (updatedSupplier != null) {
                return ResponseEntity.ok(updatedSupplier);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 5. XÓA / NGỪNG HỢP TÁC (Khớp với axios.delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}