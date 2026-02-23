package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.ProductDAO;
import com.poly.backend.entity.Product;
import com.poly.backend.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;

    public List<Product> findAll() {
        return productDAO.findAll();
    }

    public Product findById(Integer id) {
        return productDAO.findById(id).orElse(null);
    }

    public Product save(Product product) {
        return productDAO.save(product);
    }

    public void deleteById(Integer id) {
        productDAO.deleteById(id);
    }
}