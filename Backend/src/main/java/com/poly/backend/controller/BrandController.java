package com.poly.backend.controller;

import com.poly.backend.dao.BrandDAO;
import com.poly.backend.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BrandController {
    private final BrandDAO brandDAO;

    @GetMapping
    public List<Brand> getAll() {
        return brandDAO.findAll();
    }
}