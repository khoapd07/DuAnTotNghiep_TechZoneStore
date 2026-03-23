package com.poly.backend.dao;

import com.poly.backend.dto.BrandDTO;
import com.poly.backend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandDAO extends JpaRepository<Brand, Integer> {

    boolean existsByBrandName(String brandName);
    boolean existsByBrandNameAndBrandIdNot(String brandName, Integer brandId);

    // ĐÃ SỬA LỖI: Join thêm bảng variants (v) để SUM cột stockQuantity của biến thể
    @Query("SELECT new com.poly.backend.dto.BrandDTO(" +
            "b.brandId, b.brandName, COUNT(DISTINCT p.productId), COALESCE(SUM(v.stockQuantity), 0L)) " +
            "FROM Brand b " +
            "LEFT JOIN Product p ON p.brand = b " +
            "LEFT JOIN p.variants v " +
            "GROUP BY b.brandId, b.brandName")
    List<BrandDTO> findAllWithStats();
}