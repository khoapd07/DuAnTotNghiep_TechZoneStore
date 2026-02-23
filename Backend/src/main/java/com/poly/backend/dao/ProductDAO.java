package com.poly.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poly.backend.entity.Product;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {
}