package com.poly.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poly.backend.entity.Category;

@Repository
public interface CategoryDAO extends JpaRepository<Category, Integer> {

    // Kiểm tra tên trùng lúc tạo mới
    boolean existsByCategoryName(String categoryName);

    // Kiểm tra tên trùng lúc cập nhật (Loại trừ ID của chính nó)
    boolean existsByCategoryNameAndCategoryIdNot(String categoryName, Integer categoryId);
}