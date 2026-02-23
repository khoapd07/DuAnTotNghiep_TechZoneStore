package com.poly.backend.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.poly.backend.dao.ReviewDAO;
import com.poly.backend.entity.Review;
import com.poly.backend.service.ReviewService;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDAO reviewDAO;

    public List<Review> findAll() {
        return reviewDAO.findAll();
    }

    public Review findById(Integer id) {
        return reviewDAO.findById(id).orElse(null);
    }

    public Review save(Review review) {
        return reviewDAO.save(review);
    }

    public void deleteById(Integer id) {
        reviewDAO.deleteById(id);
    }
}