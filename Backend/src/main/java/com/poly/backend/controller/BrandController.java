package com.poly.backend.controller;

import com.poly.backend.dto.BrandDTO;
import com.poly.backend.entity.Brand;
import com.poly.backend.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<List<BrandDTO>> getAll() {
        return ResponseEntity.ok(brandService.findAllWithStats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> getById(@PathVariable Integer id) {
        Brand brand = brandService.findById(id);
        if (brand == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(brand);
    }

    @PostMapping
    public ResponseEntity<?> createBrand(@Valid @RequestBody BrandDTO brandDTO) {
        try {
            Brand newBrand = new Brand();
            newBrand.setBrandName(brandDTO.getBrandName());

            Brand savedBrand = brandService.create(newBrand);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBrand);
        } catch (IllegalArgumentException e) {
            // Trả về lỗi 400 kèm định dạng JSON
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Lỗi hệ thống khi thêm mới thương hiệu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Integer id, @Valid @RequestBody BrandDTO brandDTO) {
        try {
            Brand brandData = new Brand();
            brandData.setBrandName(brandDTO.getBrandName());

            Brand updatedBrand = brandService.update(id, brandData);
            return ResponseEntity.ok(updatedBrand);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Lỗi hệ thống khi cập nhật thương hiệu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Integer id) {
        try {
            Brand existingBrand = brandService.findById(id);
            if (existingBrand == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(java.util.Map.of("message", "Không tìm thấy thương hiệu để xóa"));
            }

            brandService.deleteById(id);
            return ResponseEntity.ok().body(java.util.Map.of("message", "Xóa thành công"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(java.util.Map.of("message", "Không thể xóa thương hiệu này vì đang chứa sản phẩm! Vui lòng xóa sản phẩm trước."));
        }
    }
}