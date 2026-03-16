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
    Page<ProductDTO> getProducts(String keyword, Integer categoryId, Integer brandId, BigDecimal minPrice, BigDecimal maxPrice, int page, int size, String sortBy, String sortDir);
    ProductDTO getProductById(Integer id);

    // Thêm mới và Cập nhật
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(Integer id, ProductDTO productDTO);

    // --- CÁC HÀM LẤY SẢN PHẨM CHO TRANG CHỦ ---
    List<ProductDTO> getDiscountedProducts();
    List<ProductDTO> getTop8NewestProducts();

    // --- CÁC HÀM MỚI CHO STATS VÀ FEATURED ---
    long getTotalProductsCount();
    long getLowStockProductsCount();
    Long getTotalStockQuantity();
    List<ProductDTO> getFeaturedProducts();

    Page<ProductDTO> getProducts(String keyword, Integer categoryId, Integer brandId, BigDecimal minPrice, BigDecimal maxPrice, Boolean isSale, int page, int size, String sortBy, String sortDir);
}