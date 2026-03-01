package com.poly.backend.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import com.poly.backend.entity.Product;
import com.poly.backend.dto.ProductDTO;

public interface ProductService {
    // --- CÁC HÀM CŨ (Dùng nội bộ) ---
    List<Product> findAll();
    Product findById(Integer id);
    Product save(Product product);
    void deleteById(Integer id);

    // --- CÁC HÀM MỚI DÀNH CHO API (Chỉ dùng DTO) ---
    Page<ProductDTO> getProducts(String keyword, BigDecimal minPrice, BigDecimal maxPrice, int page, int size, String sortBy, String sortDir);
    ProductDTO getProductById(Integer id);

    // Thêm mới và Cập nhật
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(Integer id, ProductDTO productDTO);
}