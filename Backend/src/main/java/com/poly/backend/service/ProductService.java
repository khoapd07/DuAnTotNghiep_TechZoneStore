package com.poly.backend.service;

import java.math.BigDecimal;
import java.util.List;

import com.poly.backend.dto.ProductDTO;
import com.poly.backend.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    List<Product> findAll();
    Product findById(Integer id);
    Product save(Product product);
    void deleteById(Integer id);

    // Lấy danh sách phân trang và lọc (Trả về DTO)
    Page<ProductDTO> getProducts(String keyword, BigDecimal minPrice, BigDecimal maxPrice, int page, int size, String sortBy, String sortDir);

    // Lấy chi tiết sản phẩm (Trả về DTO)
    ProductDTO getProductById(Integer id);
}