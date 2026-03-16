package com.poly.backend.controller;

import com.poly.backend.entity.Category;
import com.poly.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getOne(@PathVariable Integer id) {
        Category category = categoryService.findById(id);
        if (category != null) {
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Category category) {
        try {
            Category savedCategory = categoryService.create(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response); // Trả về lỗi 400 kèm thông báo
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.update(id, category);
            return ResponseEntity.ok(updatedCategory);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Category existingCategory = categoryService.findById(id);
        if (existingCategory != null) {
            try {
                categoryService.deleteById(id);
                return ResponseEntity.ok().build();
            } catch (DataIntegrityViolationException e) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Không thể xóa danh mục này vì đang chứa sản phẩm! Vui lòng xóa sản phẩm trước.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            } catch (Exception e) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Đã xảy ra lỗi hệ thống khi xóa.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
        return ResponseEntity.notFound().build();
    }
}