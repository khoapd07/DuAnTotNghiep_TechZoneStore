package com.poly.backend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.poly.backend.entity.Product;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE " +
            "(:kw IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :kw, '%'))) AND " +
            "(:cId IS NULL OR p.category.categoryId = :cId) AND " +
            "(:bId IS NULL OR p.brand.brandId = :bId) AND " +
            "(:min IS NULL OR p.price >= :min) AND " +
            "(:max IS NULL OR p.price <= :max)")
    Page<Product> searchAndFilterProducts(
            @Param("kw") String keyword,
            @Param("cId") Integer categoryId,
            @Param("bId") Integer brandId,
            @Param("min") BigDecimal minPrice,
            @Param("max") BigDecimal maxPrice,
            Pageable pageable);

    Long countByStockQuantityLessThan(Integer threshold);

    // MỚI THÊM: Tính tổng số lượng tồn kho của tất cả sản phẩm
    @Query("SELECT SUM(p.stockQuantity) FROM Product p")
    Long sumTotalStockQuantity();

    // MỚI THÊM: Kiểm tra tên trùng lúc tạo mới
    boolean existsByName(String name);

    // MỚI THÊM: Kiểm tra tên trùng lúc cập nhật (Loại trừ ID của chính nó)
    boolean existsByNameAndProductIdNot(String name, Integer productId);

    @Query("SELECT od.product FROM OrderDetail od WHERE od.order.status.statusName = 'HOÀN THÀNH' OR od.order.status.statusName = 'Delivered' GROUP BY od.product ORDER BY SUM(od.quantity) DESC")
    List<Product> findBestSellingProducts(Pageable pageable);

    List<Product> findTop2ByOrderByPriceDesc();

    @Query("SELECT p FROM Product p WHERE " +
            "(:kw IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :kw, '%'))) AND " +
            "(:cId IS NULL OR p.category.categoryId = :cId) AND " +
            "(:bId IS NULL OR p.brand.brandId = :bId) AND " +
            "(:min IS NULL OR p.price >= :min) AND " +
            "(:max IS NULL OR p.price <= :max) AND " +
            "(:isSale IS NULL OR (:isSale = true AND p.salePrice IS NOT NULL AND p.salePrice > 0))") // MỚI THÊM DÒNG NÀY
    Page<Product> searchAndFilterProducts(
            @Param("kw") String keyword,
            @Param("cId") Integer categoryId,
            @Param("bId") Integer brandId,
            @Param("min") BigDecimal minPrice,
            @Param("max") BigDecimal maxPrice,
            @Param("isSale") Boolean isSale, // MỚI THÊM PARAM NÀY
            Pageable pageable);
}