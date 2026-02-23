package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.Brand;

public interface BrandService {
    List<Brand> findAll();
    Brand findById(Integer id);
    Brand save(Brand brand);
    void deleteById(Integer id);
}