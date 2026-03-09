package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.CategoryDAO;
import com.poly.backend.entity.Category;
import com.poly.backend.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO categoryDAO;

    @Override
    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    @Override
    public Category findById(Integer id) {
        return categoryDAO.findById(id).orElse(null);
    }

    // THÊM MỚI (Có Validate trùng tên)
    @Override
    public Category create(Category category) {
        if (categoryDAO.existsByCategoryName(category.getCategoryName())) {
            throw new IllegalArgumentException("Tên danh mục này đã tồn tại!");
        }
        return categoryDAO.save(category);
    }

    // CẬP NHẬT (Có Validate trùng tên, bỏ qua chính nó)
    @Override
    public Category update(Integer id, Category category) {
        if (categoryDAO.existsByCategoryNameAndCategoryIdNot(category.getCategoryName(), id)) {
            throw new IllegalArgumentException("Tên danh mục này đã bị trùng với danh mục khác!");
        }

        Category existingCategory = categoryDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục!"));

        existingCategory.setCategoryName(category.getCategoryName());
        return categoryDAO.save(existingCategory);
    }

    @Override
    public void deleteById(Integer id) {
        categoryDAO.deleteById(id);
    }
}