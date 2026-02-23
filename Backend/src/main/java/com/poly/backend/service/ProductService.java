package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.Product;

public interface ProductService {
    List<Product> findAll();
    Product findById(Integer id);
    Product save(Product product);
    void deleteById(Integer id);
}