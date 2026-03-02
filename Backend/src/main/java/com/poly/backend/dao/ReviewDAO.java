package com.poly.backend.dao;

import com.poly.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewDAO extends JpaRepository<Review, Integer> {

    // ✅ Fix lỗi "No property 'id' found for type 'Product'"
    @Query("SELECT r FROM Review r WHERE r.product.productId = :productId ORDER BY r.reviewDate DESC")
    List<Review> findByProductId(@Param("productId") Integer productId);
}