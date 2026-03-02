package com.poly.backend.controller;

import com.poly.backend.dao.CategoryDAO;
import com.poly.backend.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CategoryController {
    private final CategoryDAO categoryDAO;

    @GetMapping
    public List<Category> getAll() {
        return categoryDAO.findAll();
    }
}