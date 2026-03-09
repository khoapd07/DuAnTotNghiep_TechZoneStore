package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.Category;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Integer id);
    Category create(Category category); // Hàm tạo mới
    Category update(Integer id, Category category); // Hàm cập nhật
    void deleteById(Integer id);
}