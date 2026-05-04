package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.repository.CategoryRepository;
import com.poly.backend.entity.Category;
import com.poly.backend.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category create(Category category) {
        if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new IllegalArgumentException("Tên danh mục này đã tồn tại!");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Integer id, Category category) {
        if (categoryRepository.existsByCategoryNameAndCategoryIdNot(category.getCategoryName(), id)) {
            throw new IllegalArgumentException("Tên danh mục này đã bị trùng với danh mục khác!");
        }

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục!"));

        existingCategory.setCategoryName(category.getCategoryName());
        // Đã xóa thuộc tính attributes cũ ở đây
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }
}