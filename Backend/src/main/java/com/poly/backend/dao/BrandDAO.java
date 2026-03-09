package com.poly.backend.dao;

import com.poly.backend.dto.BrandDTO;
import com.poly.backend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandDAO extends JpaRepository<Brand, Integer> {

    // Chú ý phần 0L để ép kiểu trả về là Long cho trùng khớp với BrandDTO
    @Query("SELECT new com.poly.backend.dto.BrandDTO(" +
            "b.brandId, b.brandName, " +
            "COUNT(p.productId), " +
            "COALESCE(SUM(p.stockQuantity), 0L)) " +
            "FROM Brand b LEFT JOIN Product p ON p.brand = b " +
            "GROUP BY b.brandId, b.brandName")
    List<BrandDTO> findAllWithStats();
}