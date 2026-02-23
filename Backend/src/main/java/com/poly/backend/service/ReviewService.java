package com.poly.backend.service;

import java.util.List;
import com.poly.backend.entity.Review;

public interface ReviewService {
    List<Review> findAll();
    Review findById(Integer id);
    Review save(Review review);
    void deleteById(Integer id);
}