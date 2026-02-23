package com.poly.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.poly.backend.entity.Category;

@Repository
public interface CategoryDAO extends JpaRepository<Category, Integer> {
}