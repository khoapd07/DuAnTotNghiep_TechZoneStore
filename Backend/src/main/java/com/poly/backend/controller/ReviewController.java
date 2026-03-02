package com.poly.backend.controller;

import com.poly.backend.dao.ReviewDAO;
import com.poly.backend.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewDAO reviewDAO;

    @PostMapping("/send")
    public ResponseEntity<?> sendReview(@RequestBody Review review) {
        try {
            // Lưu đánh giá vào DB
            Review savedReview = reviewDAO.save(review);
            return ResponseEntity.ok("Gửi đánh giá thành công! ID: " + savedReview.getReviewId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
}