package com.poly.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poly.backend.entity.Brand;

@Repository
public interface BrandDAO extends JpaRepository<Brand, Integer> {
}