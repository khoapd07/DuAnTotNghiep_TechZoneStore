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

    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    public Category findById(Integer id) {
        return categoryDAO.findById(id).orElse(null);
    }

    public Category save(Category category) {
        return categoryDAO.save(category);
    }

    public void deleteById(Integer id) {
        categoryDAO.deleteById(id);
    }
}