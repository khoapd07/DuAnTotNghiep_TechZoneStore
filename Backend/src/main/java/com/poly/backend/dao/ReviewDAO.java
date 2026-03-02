package com.poly.backend.dao;

import com.poly.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewDAO extends JpaRepository<Review, Integer> {
    // Các phương thức CRUD cơ bản đã có sẵn trong JpaRepository
}