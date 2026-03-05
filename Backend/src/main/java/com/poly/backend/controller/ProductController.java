package com.poly.backend.controller;

import com.poly.backend.dao.ProductDAO; // MỚI THÊM
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dto.ProductDTO;
import com.poly.backend.service.ProductService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;
    private final ProductDAO productDAO; // MỚI THÊM: Inject DAO để lấy thống kê nhanh

    // ==================== ENDPOINT MỚI: LẤY THỐNG KÊ ====================
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getProductStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", productDAO.count()); // Tổng số sản phẩm trong DB
        // Đếm số lượng sản phẩm có tồn kho DƯỚI 15 (nghĩa là < 15)
        stats.put("lowStock", productDAO.countByStockQuantityLessThan(16));
        return ResponseEntity.ok(stats);
    }
    // ====================================================================

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "brandId", required = false) Integer brandId,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir
    ) {
        Page<ProductDTO> products = productService.getProducts(keyword, categoryId, brandId, minPrice, maxPrice, page, size, sortBy, sortDir);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) {
        ProductDTO productDTO = productService.getProductById(id);
        return ResponseEntity.ok(productDTO);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // Lấy danh sách sản phẩm CÓ GIẢM GIÁ (Dùng cho Flash Sale)
    @GetMapping("/discounted")
    public ResponseEntity<List<ProductDTO>> getDiscountedProducts() {
        return ResponseEntity.ok(productService.getDiscountedProducts());
    }

    // Lấy 8 sản phẩm MỚI NHẤT (Dùng cho mục Hàng Mới Về)
    @GetMapping("/latest")
    public ResponseEntity<List<ProductDTO>> getLatestProducts() {
        return ResponseEntity.ok(productService.getTop8NewestProducts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
        productService.deleteById(id);
        return ResponseEntity.ok("Đã xóa sản phẩm thành công với ID: " + id);
    }
}