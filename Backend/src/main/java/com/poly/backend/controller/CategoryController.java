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
@CrossOrigin("*") // Make sure CORS is properly configured for local dev
public class CategoryController {

    // Always inject Service layer, NOT DAO repository directly in controller
    private final CategoryService categoryService;

    // GET ALL: /api/categories
    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    // GET ONE: /api/categories/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Category> getOne(@PathVariable Integer id) {
        Category category = categoryService.findById(id);
        if (category != null) {
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.notFound().build();
    }

    // CREATE (POST): /api/categories
    // The previous error 405 was because this was missing
    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    // UPDATE (PUT): /api/categories/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Integer id, @RequestBody Category category) {
        Category existingCategory = categoryService.findById(id);
        if (existingCategory != null) {
            // Update the ID to make sure we overwrite the correct record
            category.setCategoryId(id);
            Category updatedCategory = categoryService.save(category);
            return ResponseEntity.ok(updatedCategory);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE: /api/categories/{id}
    // The previous error 404 was likely because this route was missing
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Category existingCategory = categoryService.findById(id);
        if (existingCategory != null) {
            try {
                categoryService.deleteById(id);
                return ResponseEntity.ok().build();
            } catch (DataIntegrityViolationException e) {
                // Tạo một object JSON chuẩn bằng Map
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