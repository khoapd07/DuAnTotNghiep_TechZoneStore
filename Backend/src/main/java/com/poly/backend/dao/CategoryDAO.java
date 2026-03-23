package com.poly.backend.dao;

import com.poly.backend.dto.CategoryDTO;
import com.poly.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDAO extends JpaRepository<Category, Integer> {

    boolean existsByCategoryName(String categoryName);
    boolean existsByCategoryNameAndCategoryIdNot(String categoryName, Integer categoryId);

    // ĐÃ SỬA LỖI: Join thêm bảng variants (v) để SUM cột stockQuantity của biến thể
    @Query("SELECT new com.poly.backend.dto.CategoryDTO(" +
            "c.categoryId, c.categoryName, COUNT(DISTINCT p.productId), COALESCE(SUM(v.stockQuantity), 0L)) " +
            "FROM Category c " +
            "LEFT JOIN Product p ON p.category = c " +
            "LEFT JOIN p.variants v " +
            "GROUP BY c.categoryId, c.categoryName")
    List<CategoryDTO> findAllWithStats();
}