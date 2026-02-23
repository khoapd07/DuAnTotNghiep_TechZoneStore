package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.Category;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Integer id);
    Category save(Category category);
    void deleteById(Integer id);
}