package com.poly.backend.controller;

import com.poly.backend.dto.BrandDTO;
import com.poly.backend.entity.Brand;
import com.poly.backend.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BrandController {

    private final BrandService brandService;

    // 1. GET ALL (Kèm thống kê số lượng SP và Tồn kho)
    @GetMapping
    public ResponseEntity<List<BrandDTO>> getAll() {
        return ResponseEntity.ok(brandService.findAllWithStats());
    }

    // 2. GET BY ID (Lấy thông tin 1 thương hiệu để edit)
    @GetMapping("/{id}")
    public ResponseEntity<Brand> getById(@PathVariable Integer id) {
        Brand brand = brandService.findById(id);
        if (brand == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(brand);
    }

    // 3. POST (Thêm mới thương hiệu)
    @PostMapping
    public ResponseEntity<?> createBrand(@Valid @RequestBody BrandDTO brandDTO) {
        try {
            // Chuyển dữ liệu từ DTO sang Entity để lưu vào DB
            Brand newBrand = new Brand();
            newBrand.setBrandName(brandDTO.getBrandName());

            Brand savedBrand = brandService.save(newBrand);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBrand);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi khi thêm mới thương hiệu");
        }
    }

    // 4. PUT (Cập nhật thương hiệu)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Integer id, @Valid @RequestBody BrandDTO brandDTO) {
        try {
            Brand existingBrand = brandService.findById(id);
            if (existingBrand == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy thương hiệu");
            }

            // Cập nhật tên thương hiệu mới
            existingBrand.setBrandName(brandDTO.getBrandName());

            Brand updatedBrand = brandService.save(existingBrand);
            return ResponseEntity.ok(updatedBrand);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi khi cập nhật thương hiệu");
        }
    }

    // 5. DELETE (Xóa thương hiệu)
    // 5. DELETE (Xóa thương hiệu)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Integer id) {
        try {
            Brand existingBrand = brandService.findById(id);
            if (existingBrand == null) {
                // Trả về dạng JSON {"message": "..."}
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(java.util.Map.of("message", "Không tìm thấy thương hiệu để xóa"));
            }

            brandService.deleteById(id);
            return ResponseEntity.ok().body(java.util.Map.of("message", "Xóa thành công"));

        } catch (Exception e) {
            // Sửa thông báo lỗi ở đây để trả về dạng JSON
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(java.util.Map.of("message", "Không thể xóa thương hiệu này vì đang chứa sản phẩm! Vui lòng xóa sản phẩm trước."));
        }
    }
}