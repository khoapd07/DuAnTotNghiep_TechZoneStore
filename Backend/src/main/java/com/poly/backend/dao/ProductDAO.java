package com.poly.backend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.poly.backend.entity.Product;

import java.math.BigDecimal;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {
    // Lọc theo tên, khoảng giá (ưu tiên sale_price) và phân trang
    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:minPrice IS NULL OR COALESCE(p.salePrice, p.price) >= :minPrice) AND " +
            "(:maxPrice IS NULL OR COALESCE(p.salePrice, p.price) <= :maxPrice) AND " +
            "(p.active = true)")
    Page<Product> searchAndFilterProducts(
            @Param("keyword") String keyword,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);
}